package com.github.kantis.mikrom

import com.github.kantis.mikrom.datasource.Transaction
import org.intellij.lang.annotations.Language
import kotlin.collections.emptyList
import kotlin.jvm.JvmInline
import kotlin.reflect.KClass

@JvmInline
public value class Query(
   @Language("SQL") public val value: String,
)

public val nonMappedPrimitives: Set<KClass<*>> = setOf(
   String::class,
   Int::class,
   Long::class,
   Float::class,
   Double::class,
   Boolean::class,
)

public inline fun <reified T> Mikrom.queryForSingleOrNull(query: Query): T? = null

context(Transaction)
public inline fun <reified T> Mikrom.queryFor(query: Query): List<T> {
   if (T::class in nonMappedPrimitives) {
      return query(query).map { it.values.single() as T }
   }
   val rowMapper = resolveRowMapper<T>()
   return query(query).map(rowMapper::mapRow)
}

context(Transaction)
public inline fun <reified T> Mikrom.queryFor(
   query: Query,
   param: Any,
): List<T> = queryFor(query, listOf(param))

context(Transaction)
public inline fun <reified T> Mikrom.queryFor(
   query: Query,
   params: List<Any>,
): List<T> {
   if (T::class in nonMappedPrimitives) {
      return query(query, params).map { it.values.single() as T }
   }
   val rowMapper = resolveRowMapper<T>()
   return query(query, params).map(rowMapper::mapRow)
}

context(Transaction)
public inline fun Mikrom.execute(
   query: Query,
   params: List<Any>,
) {
   executeInTransaction(query, params)
}

context(Transaction)
public fun Mikrom.execute(
   query: Query,
   vararg params: List<Any>,
) {
   params.forEach { executeInTransaction(query, it) }
}

context(Transaction)
public fun <T : Any> Mikrom.execute(
   query: Query,
   vararg params: T,
) {
   params.forEach { if (it is List<*>) executeInTransaction(query, it) else executeInTransaction(query, listOf(it)) }
}

context(Transaction)
public fun Mikrom.execute(query: Query) {
   executeInTransaction(query, emptyList<Any>())
}
