package com.github.kantis.kapper

interface DataSource {
   fun transaction(block: Transaction.() -> Unit)
}

interface Transaction {
   fun executeInTransaction(
      query: Query,
      vararg params: List<Any>,
   )

   fun query(
      query: Query,
      params: List<Any> = emptyList(),
   ): List<Row>
}
