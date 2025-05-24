package com.github.kantis.kapper.jdbc.h2

import com.github.kantis.kapper.jdbc.JdbcDataSource
import org.h2.Driver
import java.sql.DriverManager

object H2Helpers {
   fun prepareDatabase(
      connectionString: String,
      vararg statements: String,
   ): JdbcDataSource {
      DriverManager.registerDriver(Driver())
      val jdbcConnection = DriverManager.getConnection(connectionString)
      jdbcConnection.beginRequest()
      statements.forEach {
         jdbcConnection.createStatement().use { statement ->
            statement.execute(it)
         }
      }
      jdbcConnection.commit()
      jdbcConnection.endRequest()
      return JdbcDataSource(jdbcConnection)
   }
}
