package com.github.kantis.mikrom.jdbc

import com.github.kantis.mikrom.MikromInternal
import com.github.kantis.mikrom.Row
import java.sql.ResultSet
import java.sql.Types

@MikromInternal
public object ResultSetReader {
   @MikromInternal
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
            Types.TIME -> resultSet.getTime(i)
            Types.TIME_WITH_TIMEZONE -> TODO("TIME_WITH_TIMEZONE is not supported yet")
            Types.TIMESTAMP -> resultSet.getTimestamp(i)
            Types.TIMESTAMP_WITH_TIMEZONE -> TODO("TIMESTAMP_WITH_TIMEZONE is not supported yet")
            else -> error("Column $i is of unsupported type $columnType")
         }

         row[columnName] = value
      }
      return row
   }
}
