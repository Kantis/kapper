package com.github.kantis.mikrom

public fun interface ParameterMapper<in T> {
   public fun toParams(value: T): Map<String, Any>
}
