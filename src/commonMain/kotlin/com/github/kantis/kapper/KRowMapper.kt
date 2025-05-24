package com.github.kantis.kapper

fun interface KRowMapper<out T> {
   fun mapRow(row: Row): T
}
