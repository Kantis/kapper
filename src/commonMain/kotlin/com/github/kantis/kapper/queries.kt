package com.github.kantis.kapper

import kotlin.jvm.JvmInline

@JvmInline
value class Query(val value: String)

inline fun <reified T> Kapper.queryForSingleOrNull(query: Query): T? {
   return null
}

context(Transaction)
inline fun <reified T> Kapper.queryFor(query: Query): List<T> {
   val rowMapper = resolveMapper<T>()
   return execute(query).map(rowMapper::mapRow)
}
