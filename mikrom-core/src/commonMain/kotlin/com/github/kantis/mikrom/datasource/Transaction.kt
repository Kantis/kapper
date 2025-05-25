package com.github.kantis.mikrom.datasource

import com.github.kantis.mikrom.Query
import com.github.kantis.mikrom.Row

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
