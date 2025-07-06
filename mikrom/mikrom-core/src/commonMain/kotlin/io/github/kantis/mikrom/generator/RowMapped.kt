package io.github.kantis.mikrom.generator

/**
 * Specifies that the Mikrom compiler plugin should generate both a [RowMapper] for the annotated class.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
public annotation class RowMapped
