package com.github.kantis.kapper.jdbc

import com.github.kantis.kapper.DataSource
import com.github.kantis.kapper.Transaction
import java.sql.Connection

class JdbcDataSource(val jdbcConnection: Connection) : DataSource {
   override fun transaction(block: Transaction.() -> Unit) {
      jdbcConnection.beginRequest()
      try {
         val transaction = Transaction { query ->
            jdbcConnection
               .prepareCall(query.value)
               .executeQuery()
               .let(ResultSetReader::loadResultSet)
         }

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
