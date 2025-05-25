package com.github.kantis.mikrom

public fun interface KRowMapper<out T> {
   public fun mapRow(row: Row): T
}
