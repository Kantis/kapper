package com.github.kantis.kapper

public interface DataSource {
   public fun transaction(block: Transaction.() -> Unit)
}

public interface Transaction {
   public fun executeInTransaction(
      query: Query,
      vararg params: List<Any>,
   )

   public fun query(
      query: Query,
      params: List<Any> = emptyList(),
   ): List<Row>
}
