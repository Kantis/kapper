package com.github.kantis.mikrom.generator

/**
 * Specifies that the Mikrom compiler plugin should generate both a [RowMapper] and a [ParameterMapper] for the annotated class.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class AutoMapper
