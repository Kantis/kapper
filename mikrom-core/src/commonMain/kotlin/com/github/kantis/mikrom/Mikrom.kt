package com.github.kantis.mikrom

import kotlin.reflect.KClass

public class Mikrom(
   public val rowMappers: MutableMap<KClass<*>, KRowMapper<*>>,
) {
   @Suppress("UNCHECKED_CAST")
   public inline fun <reified T> resolveMapper(): KRowMapper<T> = rowMappers[T::class] as KRowMapper<T>

   public companion object {
      public operator fun invoke(builder: MikromBuilder.() -> Unit): Mikrom = MikromBuilder().apply(builder).build()
   }
}
