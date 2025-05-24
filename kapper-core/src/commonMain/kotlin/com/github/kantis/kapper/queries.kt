package com.github.kantis.kapper

import kotlin.jvm.JvmInline

@JvmInline
value class Query(val value: String)

inline fun <reified T> Kapper.queryForSingleOrNull(query: Query): T? = null

context(Transaction)
inline fun <reified T> Kapper.queryFor(query: Query): List<T> {
   val rowMapper = resolveMapper<T>()
   return query(query).map(rowMapper::mapRow)
}

context(Transaction)
inline fun <reified T> Kapper.queryFor(
   query: Query,
   params: List<Any>,
): List<T> {
   val rowMapper = resolveMapper<T>()
   return query(query, params).map(rowMapper::mapRow)
}

context(Transaction)
inline fun <reified T> Kapper.execute(
   query: Query,
   params: List<Any>,
) {
   executeInTransaction(query, params)
}

context(Transaction)
inline fun <reified T> Kapper.execute(
   query: Query,
   vararg params: List<Any>,
) {
   params.forEach { executeInTransaction(query, it) }
}

context(Transaction)
inline fun <reified T> Kapper.execute(
   query: Query,
   vararg params: Any,
) {
   params.forEach { executeInTransaction(query, listOf(it)) }
}
