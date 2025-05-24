package com.github.kantis.kapper

import kotlin.reflect.KClass

class Kapper(
   val rowMappers: MutableMap<KClass<*>, KRowMapper<*>>,
) {
   @Suppress("UNCHECKED_CAST")
   inline fun <reified T> resolveMapper(): KRowMapper<T> = rowMappers[T::class] as KRowMapper<T>

   companion object {
      operator fun invoke(builder: KapperBuilder.() -> Unit) = KapperBuilder().apply(builder).build()
   }
}
