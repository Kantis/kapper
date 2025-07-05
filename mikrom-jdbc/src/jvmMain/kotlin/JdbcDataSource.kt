package com.github.kantis.mikrom.jdbc

import com.github.kantis.mikrom.Query
import com.github.kantis.mikrom.Row
import com.github.kantis.mikrom.datasource.DataSource
import com.github.kantis.mikrom.datasource.Rollback
import com.github.kantis.mikrom.datasource.Transaction
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.PreparedStatement
import java.time.LocalDateTime

public class JdbcTransaction(private val connection: Connection) : Transaction {
   override fun executeInTransaction(
      query: Query,
      vararg parameterLists: List<*>,
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
      params: List<*>,
   ): List<Row> =
      connection.prepareStatement(query.value).use { statement ->
         bindParameters(statement, params)
         statement.executeQuery().let(ResultSetReader::loadResultSet)
      }

   private fun bindParameters(
      statement: PreparedStatement,
      params: List<*>,
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
            is LocalDateTime -> statement.setTimestamp(index + 1, java.sql.Timestamp.valueOf(param))
            null -> statement.setNull(index + 1, java.sql.Types.NULL)
            else -> error("Unsupported parameter type: ${param::class.simpleName} at index ${index + 1} with value $param")
         }
      }
   }
}

public class JdbcDataSource(private val underlyingDataSource: javax.sql.DataSource) : DataSource {
   override fun <T> transaction(block: Transaction.() -> T): T {
      underlyingDataSource.connection.use { jdbcConnection ->
         jdbcConnection.autoCommit = false
         jdbcConnection.beginRequest()
         return try {
            val transaction = JdbcTransaction(jdbcConnection)
            val result = transaction.block()

            when (result) {
               is Rollback -> {
                  logger.info("Rolling back transaction, since transaction resulted in TransactionResult.Rollback")
                  jdbcConnection.rollback()
               }

               else -> {
                  logger.info("Committing transaction, since transaction resulted in TransactionResult.Commit")
                  jdbcConnection.commit()
               }
            }

            result
         } catch (e: Exception) {
            logger.warn("Rolling back transaction, since transaction resulted in unhandled exception: {}", e.message)
            jdbcConnection.rollback()
            throw e
         } finally {
            logger.debug("Closing JDBC request")
            jdbcConnection.endRequest()
         }
      }
   }

   public companion object {
      private val logger = LoggerFactory.getLogger(JdbcDataSource::class.java)
   }
}
