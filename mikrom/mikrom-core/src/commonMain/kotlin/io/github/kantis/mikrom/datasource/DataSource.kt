package io.github.kantis.mikrom.datasource

public interface DataSource {
   public fun <T> transaction(block: Transaction.() -> T): T
}

public interface SuspendingDataSource {
   public suspend fun <T> suspendingTransaction(block: suspend SuspendingTransaction.() -> T): T
}
