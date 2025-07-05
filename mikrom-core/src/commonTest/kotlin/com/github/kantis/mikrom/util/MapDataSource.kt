package com.github.kantis.mikrom.util

import com.github.kantis.mikrom.Query
import com.github.kantis.mikrom.Row
import com.github.kantis.mikrom.datasource.DataSource
import com.github.kantis.mikrom.datasource.Transaction

class InMemoryDataSource(
   val rows: List<Row>,
) : DataSource {
   override fun <T> transaction(block: Transaction.() -> T): T {
      val transaction = InMemoryTransaction(rows)

      return transaction.block()
   }
}

class InMemoryTransaction(val rows: List<Row>) : Transaction {
   override fun executeInTransaction(
      query: Query,
      vararg params: List<Any>,
   ) {
      println("Received query ${query.value}")
   }

   override fun query(
      query: Query,
      params: List<Any>,
   ): List<Row> = rows
}
