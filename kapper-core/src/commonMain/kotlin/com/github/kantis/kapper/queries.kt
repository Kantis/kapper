package com.github.kantis.kapper

import kotlin.jvm.JvmInline

@JvmInline
public value class Query(public val value: String)

public inline fun <reified T> Kapper.queryForSingleOrNull(query: Query): T? = null

context(Transaction)
public inline fun <reified T> Kapper.queryFor(query: Query): List<T> {
   val rowMapper = resolveMapper<T>()
   return query(query).map(rowMapper::mapRow)
}

context(Transaction)
public inline fun <reified T> Kapper.queryFor(
   query: Query,
   param: Any,
): List<T> = queryFor(query, listOf(param))

context(Transaction)
public inline fun <reified T> Kapper.queryFor(
   query: Query,
   params: List<Any>,
): List<T> {
   val rowMapper = resolveMapper<T>()
   return query(query, params).map(rowMapper::mapRow)
}

context(Transaction)
public inline fun <reified T> Kapper.execute(
   query: Query,
   params: List<Any>,
) {
   executeInTransaction(query, params)
}

context(Transaction)
public inline fun Kapper.execute(
   query: Query,
   vararg params: List<Any>,
) {
   params.forEach { executeInTransaction(query, it) }
}

context(Transaction)
public inline fun Kapper.execute(
   query: Query,
   vararg params: Any,
) {
   params.forEach { executeInTransaction(query, listOf(it)) }
}
