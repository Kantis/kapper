package com.github.kantis.kapper

import kotlin.reflect.KClass

public class KapperBuilder {
   public val rowMappers: MutableMap<KClass<*>, KRowMapper<*>> = mutableMapOf()

   public inline fun <reified T> registerMapper(mapper: KRowMapper<T>) {
      rowMappers.put(T::class, mapper)
   }

   public fun build(): Kapper = Kapper(rowMappers)
}
