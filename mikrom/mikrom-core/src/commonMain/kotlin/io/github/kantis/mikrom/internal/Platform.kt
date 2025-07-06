package io.github.kantis.mikrom.internal

import io.github.kantis.mikrom.RowMapper
import kotlin.reflect.KClass

public expect fun <T : Any> KClass<T>.compiledRowMapper(): RowMapper<T>?
