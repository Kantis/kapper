package com.github.kantis.mikrom

import org.intellij.lang.annotations.Language

@JvmInline
public value class Query(
    @Language("SQL") public val value: String,
)
