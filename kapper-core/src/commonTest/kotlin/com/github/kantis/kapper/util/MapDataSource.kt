package com.github.kantis.kapper.util

import com.github.kantis.kapper.DataSource
import com.github.kantis.kapper.Row
import com.github.kantis.kapper.Transaction

class InMemoryDataSource(
   val rows: List<Row>,
) : DataSource {
   override fun transaction(block: Transaction.() -> Unit) {
      val transaction = Transaction { rows }

      transaction.block()
   }
}
