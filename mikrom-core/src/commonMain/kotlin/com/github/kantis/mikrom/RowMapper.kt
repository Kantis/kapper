package com.github.kantis.mikrom

public fun interface RowMapper<out T> {
   public fun mapRow(row: Row): T
}
