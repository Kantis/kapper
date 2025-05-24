package com.github.kantis.kapper

import kotlin.reflect.KClass

class KapperBuilder {
   val rowMappers: MutableMap<KClass<*>, KRowMapper<*>> = mutableMapOf()

   inline fun <reified T> registerMapper(mapper: KRowMapper<T>) {
      rowMappers.put(T::class, mapper)
   }

   fun build(): Kapper = Kapper(rowMappers)
}
