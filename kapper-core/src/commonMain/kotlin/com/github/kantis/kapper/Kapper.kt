package com.github.kantis.kapper

import kotlin.reflect.KClass

public class Kapper(
   public val rowMappers: MutableMap<KClass<*>, KRowMapper<*>>,
) {
   @Suppress("UNCHECKED_CAST")
   public inline fun <reified T> resolveMapper(): KRowMapper<T> = rowMappers[T::class] as KRowMapper<T>

   public companion object {
      public operator fun invoke(builder: KapperBuilder.() -> Unit): Kapper = KapperBuilder().apply(builder).build()
   }
}
