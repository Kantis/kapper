package io.github.kantis.mikrom.generator

/**
 * Specifies that Mikrom should generate a parameter mapper for the annotated class, to be used for setting parameters in SQL queries.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class Parameter
