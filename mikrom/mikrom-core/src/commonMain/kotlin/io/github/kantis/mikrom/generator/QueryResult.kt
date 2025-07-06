package io.github.kantis.mikrom.generator

/**
 * Annotation to mark a class as a query result type.
 *
 * Mikrom will generate a [io.github.kantis.mikrom.RowMapper] for this class based on the names of parameters to its primary constructor.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
public annotation class QueryResult
