package com.github.kantis.mikrom

import kotlin.reflect.KClass

public class Mikrom(
   public val rowMappers: MutableMap<KClass<*>, RowMapper<*>>,
   public val parameterMappers: MutableMap<KClass<*>, ParameterMapper<*>>,
) {
   @Suppress("UNCHECKED_CAST")
   public inline fun <reified T> resolveRowMapper(): RowMapper<T> = rowMappers[T::class] as RowMapper<T>

   @Suppress("UNCHECKED_CAST")
   public inline fun <reified T> resolveParameterMapper(): ParameterMapper<T> = parameterMappers[T::class] as ParameterMapper<T>

   public companion object {
      public operator fun invoke(builder: MikromBuilder.() -> Unit): Mikrom = MikromBuilder().apply(builder).build()
   }
}
