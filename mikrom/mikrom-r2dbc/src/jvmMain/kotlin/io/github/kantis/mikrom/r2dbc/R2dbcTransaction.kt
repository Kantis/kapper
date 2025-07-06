package io.github.kantis.mikrom.r2dbc

import io.github.kantis.mikrom.Query
import io.github.kantis.mikrom.Row
import io.github.kantis.mikrom.datasource.SuspendingTransaction
import io.r2dbc.spi.Connection
import io.r2dbc.spi.Statement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitLast

public class R2dbcTransaction(private val connection: Connection) : SuspendingTransaction {
   override suspend fun executeInTransaction(
      query: Query,
      params: Flow<List<*>>,
   ) {
      val statement = connection.createStatement(query.value)
      params.collect { p ->
         bindParameters(statement, p)
         statement.execute().awaitLast()
      }
   }

   override suspend fun query(
      query: Query,
      params: List<*>,
   ): Flow<Row> {
      val statement = connection.createStatement(query.value)
      bindParameters(statement, params)
      return statement.execute().asFlow().flatMapMerge { result ->
         result.map { row, rowMetadata ->
            rowMetadata.columnMetadatas.associate { metadata ->
               metadata.name to row.get(metadata.name)
            }
         }.asFlow()
      }
   }

   private fun bindParameters(
      statement: Statement,
      params: List<*>,
   ) {
      params.forEachIndexed { index, param ->
         if (param == null) {
            statement.bindNull(index + 1, Unit::class.java)
         } else {
            statement.bind(index + 1, param)
         }
      }
   }
}
