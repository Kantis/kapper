package com.github.kantis.mikrom.jdbc

import com.github.kantis.mikrom.Query
import com.github.kantis.mikrom.Row
import com.github.kantis.mikrom.datasource.DataSource
import com.github.kantis.mikrom.datasource.Transaction
import com.github.kantis.mikrom.datasource.TransactionResult
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.PreparedStatement

public class JdbcTransaction(private val connection: Connection) : Transaction {
   override fun executeInTransaction(
      query: Query,
      vararg parameterLists: List<Any>,
   ) {
      connection.prepareStatement(query.value).use { statement ->
         parameterLists.forEach { params ->
            bindParameters(statement, params)
            statement.execute()
         }
      }
   }

   override fun query(
      query: Query,
      params: List<Any>,
   ): List<Row> =
      connection.prepareStatement(query.value).use { statement ->
         bindParameters(statement, params)
         statement.executeQuery().let(ResultSetReader::loadResultSet)
      }

   private fun bindParameters(
      statement: PreparedStatement,
      params: List<Any>,
   ) {
      val statementParams = statement.parameterMetaData
      require(params.count() == statementParams.parameterCount) {
         "Expected ${statementParams.parameterCount} parameters, but got ${params.count()}"
      }

      params.forEachIndexed { index, param ->
         when (param) {
            is String -> statement.setString(index + 1, param)
            is Int -> statement.setInt(index + 1, param)
            is Long -> statement.setLong(index + 1, param)
            is Double -> statement.setDouble(index + 1, param)
            is Float -> statement.setFloat(index + 1, param)
            is Boolean -> statement.setBoolean(index + 1, param)
            is ByteArray -> statement.setBytes(index + 1, param)
            else -> error("TODO")
         }
      }
   }
}

public class JdbcDataSource(private val jdbcConnection: Connection) : DataSource {
   override fun transaction(block: Transaction.() -> TransactionResult) {
      jdbcConnection.beginRequest()
      try {
         val transaction = JdbcTransaction(jdbcConnection)

         when (transaction.block()) {
            TransactionResult.Commit -> {
               logger.debug("Committing transaction, since transaction resulted in {}", TransactionResult.Commit)
               jdbcConnection.commit()
            }

            TransactionResult.Rollback -> {
               logger.debug("Rolling back transaction, since transaction resulted in {}", TransactionResult.Rollback)
               jdbcConnection.rollback()
            }
         }
      } catch (e: Exception) {
         logger.warn("Rolling back transaction, since transaction resulted in unhandled exception: {}", e.message)
         jdbcConnection.rollback()
         throw e
      } finally {
         logger.debug("Closing JDBC request")
         jdbcConnection.endRequest()
      }
   }

   public companion object {
      private val logger = LoggerFactory.getLogger(JdbcDataSource::class.java)
   }
}
