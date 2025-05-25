package com.github.kantis.kapper.jdbc

import com.github.kantis.kapper.DataSource
import com.github.kantis.kapper.Query
import com.github.kantis.kapper.Row
import com.github.kantis.kapper.Transaction
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
   override fun transaction(block: Transaction.() -> Unit) {
      jdbcConnection.beginRequest()
      try {
         val transaction = JdbcTransaction(jdbcConnection)

         transaction.block()
         jdbcConnection.commit()
      } catch (e: Exception) {
         jdbcConnection.rollback()
         throw e
      } finally {
         jdbcConnection.endRequest()
      }
   }
}
