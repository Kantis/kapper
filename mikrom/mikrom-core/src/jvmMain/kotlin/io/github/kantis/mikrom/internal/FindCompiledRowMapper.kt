package io.github.kantis.mikrom.internal

import io.github.kantis.mikrom.RowMapper
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass

public actual fun <T : Any> KClass<T>.compiledRowMapper(): RowMapper<T>? = invokeRowMapperOnDefaultCompanion(java)

public fun <T : Any> invokeRowMapperOnDefaultCompanion(jClass: Class<*>): RowMapper<T>? =
   jClass
      .companionOrNull("Companion")
      ?.let(::invokeRowMapperOnCompanion)

@Suppress("UNCHECKED_CAST")
private fun <T : Any> invokeRowMapperOnCompanion(companion: Any): RowMapper<T>? =
   try {
      println("Invoking rowMapper on companion: ${companion.javaClass.name}")
      companion
         .javaClass
         .getDeclaredMethod("rowMapper")
         .invoke(companion) as? RowMapper<T>
   } catch (e: NoSuchMethodException) {
      null
   } catch (e: InvocationTargetException) {
      val cause = e.cause ?: throw e
      throw InvocationTargetException(cause, cause.message ?: e.message)
   }

private fun Class<*>.companionOrNull(companionName: String) =
   try {
      val companion = getDeclaredField(companionName)
      companion.isAccessible = true
      companion.get(null)
   } catch (e: Throwable) {
      null
   }
