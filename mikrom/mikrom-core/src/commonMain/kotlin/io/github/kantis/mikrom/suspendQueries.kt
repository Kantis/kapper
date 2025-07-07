package io.github.kantis.mikrom

import io.github.kantis.mikrom.datasource.SuspendingTransaction
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

context(SuspendingTransaction)
public suspend inline fun <reified T : Any> Mikrom.queryFor(query: Query): Flow<T> {
   if (T::class in nonMappedPrimitives) {
      return query(query).map { it.values.single() as T }
   }
   val rowMapper = resolveRowMapper<T>()
   return query(query).map(rowMapper::mapRow)
}

context(SuspendingTransaction)
public suspend inline fun <reified T> Mikrom.queryFor(
   query: Query,
   param: Any,
): Flow<T> = queryFor(query, listOf(param))

context(SuspendingTransaction)
public suspend inline fun <reified T : Any> Mikrom.queryFor(
   query: Query,
   params: List<Any>,
): Flow<T> {
   if (T::class in nonMappedPrimitives) {
      return query(query, params).map { it.values.single() as T }
   }
   val rowMapper = resolveRowMapper<T>()
   return query(query, params).map(rowMapper::mapRow)
}

context(SuspendingTransaction)
public suspend inline fun Mikrom.execute(
   query: Query,
   params: List<Any>,
) {
   executeInTransaction(query, flowOf(params))
}

context(SuspendingTransaction)
public suspend fun Mikrom.execute(
   query: Query,
   params: Flow<List<Any>>,
): Job {
   return executeInTransaction(query, params)
}

context(SuspendingTransaction)
public suspend fun <T : Any> Mikrom.execute(
   query: Query,
   vararg params: T,
): Job {
   return launch {
      params.forEach {
         if (it is List<*>)
            executeInTransaction(query, flowOf(it))
         else
            executeInTransaction(query, flowOf(listOf(it)))
      }
   }
}

context(SuspendingTransaction)
public suspend fun Mikrom.execute(query: Query): Job {
   return executeInTransaction(query, flowOf(emptyList<Any>()))
}
