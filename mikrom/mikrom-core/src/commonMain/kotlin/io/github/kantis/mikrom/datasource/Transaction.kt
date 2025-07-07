package io.github.kantis.mikrom.datasource

import io.github.kantis.mikrom.Query
import io.github.kantis.mikrom.Row
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

public interface Transaction {
   public fun executeInTransaction(
      query: Query,
      vararg params: List<*>,
   )

   public fun query(
      query: Query,
      params: List<*> = emptyList<Any>(),
   ): List<Row>
}

public interface SuspendingTransaction: CoroutineScope {
   public suspend fun executeInTransaction(
      query: Query,
      params: Flow<List<*>>,
   ): Job

   public suspend fun query(
      query: Query,
      params: List<*> = emptyList<Any>(),
   ): Flow<Row>
}
