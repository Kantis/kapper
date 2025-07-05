package com.github.kantis.mikrom.jdbc.h2

import com.github.kantis.mikrom.jdbc.JdbcDataSource
import com.zaxxer.hikari.HikariDataSource
import org.h2.Driver
import java.sql.DriverManager

fun prepareH2Database(vararg statements: String): JdbcDataSource {
   val connectionString = "jdbc:h2:mem:test;IGNORECASE=true;MODE=MYSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;"
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
   return JdbcDataSource(HikariDataSource())
}
