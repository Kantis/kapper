package com.github.kantis.kapper.util

import com.github.kantis.kapper.DataSource
import com.github.kantis.kapper.Query
import com.github.kantis.kapper.Row
import com.github.kantis.kapper.Transaction

class InMemoryDataSource(
   val rows: List<Row>,
) : DataSource {
   override fun transaction(block: Transaction.() -> Unit) {
      val transaction = InMemoryTransaction(rows)

      transaction.block()
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
