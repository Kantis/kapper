package com.github.kantis.kapper.jdbc

import com.github.kantis.kapper.KapperInternal
import com.github.kantis.kapper.Row
import java.sql.ResultSet
import java.sql.Types

@KapperInternal
public object ResultSetReader {
   @KapperInternal
   public fun loadResultSet(resultSet: ResultSet): List<Row> {
      val rows = mutableListOf<Row>()
      try {
         while (resultSet.next()) {
            rows.add(loadRow(resultSet))
         }
      } finally {
         resultSet.close()
      }
      return rows
   }

   private fun loadRow(resultSet: ResultSet): Row {
      val row = mutableMapOf<String, Any?>()
      val metaData = resultSet.metaData
      for (i in 1..metaData.columnCount) {
         val columnName = metaData.getColumnName(i)
         println("Reading column $i: $columnName")
         val columnType = resultSet.metaData.getColumnType(i)
         val value = when (columnType) {
            Types.BIT,
            Types.TINYINT,
            Types.SMALLINT,
            Types.INTEGER,
            Types.BIGINT,
            -> resultSet.getInt(i)
            Types.VARCHAR -> resultSet.getString(i)
            Types.BOOLEAN -> resultSet.getBoolean(i)
            Types.DATE -> resultSet.getDate(i)
            else -> error("Column $i is of unsupported type $columnType")
         }

         row[columnName] = value
      }
      return row
   }
}
