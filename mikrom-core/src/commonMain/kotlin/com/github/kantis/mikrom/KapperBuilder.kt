package com.github.kantis.mikrom

import kotlin.reflect.KClass

public class MikromBuilder {
   public val rowMappers: MutableMap<KClass<*>, KRowMapper<*>> = mutableMapOf()

   public inline fun <reified T> registerMapper(mapper: KRowMapper<T>) {
      rowMappers.put(T::class, mapper)
   }

   public fun build(): Mikrom = Mikrom(rowMappers)
}
