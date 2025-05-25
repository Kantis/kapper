package com.github.kantis.mikrom.datasource

public interface DataSource {
   public fun transaction(block: Transaction.() -> Unit)
}
