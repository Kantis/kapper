package com.github.kantis.kapper

/**
 * An internal Kapper feature that is public for operational reasons but should not be used by end users.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.TYPEALIAS)
@MustBeDocumented
@Retention(value = AnnotationRetention.BINARY)
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
public annotation class KapperInternal
