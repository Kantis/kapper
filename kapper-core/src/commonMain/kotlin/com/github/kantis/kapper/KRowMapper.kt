package com.github.kantis.kapper

public fun interface KRowMapper<out T> {
   public fun mapRow(row: Row): T
}
