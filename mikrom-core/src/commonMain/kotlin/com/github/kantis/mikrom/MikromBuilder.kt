package com.github.kantis.mikrom

import kotlin.reflect.KClass

public class MikromBuilder {
   public val rowMappers: MutableMap<KClass<*>, RowMapper<*>> = mutableMapOf()
   public val parameterMappers: MutableMap<KClass<*>, ParameterMapper<*>> = mutableMapOf()

   public inline fun <reified T> registerRowMapper(mapper: RowMapper<T>) {
      rowMappers.put(T::class, mapper)
   }

   public inline fun <reified T> registerParameterMapper(mapper: ParameterMapper<T>) {
      parameterMappers.put(T::class, mapper)
   }

   public fun build(): Mikrom = Mikrom(rowMappers, parameterMappers)
}
