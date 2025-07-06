package io.github.kantis.mikrom

import kotlin.reflect.KClass

public class MikromBuilder {
   public val rowMappers: MutableMap<KClass<*>, RowMapper<*>> = mutableMapOf()

   public inline fun <reified T> registerRowMapper(mapper: RowMapper<T>) {
      rowMappers.put(T::class, mapper)
   }

   public fun build(): Mikrom = Mikrom(rowMappers)
}
