package io.github.kantis.mikrom.r2dbc

import io.github.kantis.mikrom.datasource.Rollback
import io.github.kantis.mikrom.datasource.SuspendingDataSource
import io.github.kantis.mikrom.datasource.SuspendingTransaction
import io.r2dbc.pool.ConnectionPool
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import org.slf4j.LoggerFactory

public class PooledR2dbcDataSource(private val underlyingConnectionPool: ConnectionPool) : SuspendingDataSource {
   override suspend fun <T> suspendingTransaction(block: suspend SuspendingTransaction.() -> T): T {
      val connection = underlyingConnectionPool.create().awaitSingle()
      connection.isAutoCommit = false
      connection.beginTransaction().awaitFirstOrNull()

      return try {
         val transaction = R2dbcTransaction(connection)
         val result = transaction.block()

         when (result) {
            is Rollback -> {
               logger.info("Rolling back transaction, since transaction resulted in TransactionResult.Rollback")
               connection.rollbackTransaction().awaitFirstOrNull()
            }

            else -> {
               logger.info("Committing transaction, since transaction resulted in TransactionResult.Commit")
               connection.commitTransaction().awaitFirstOrNull()
            }
         }

         result
      } catch (e: Exception) {
         logger.warn("Rolling back transaction, since transaction resulted in unhandled exception: {}", e.message)
         connection.rollbackTransaction().awaitFirstOrNull()
         throw e
      } finally {
         logger.debug("Closing R2DBC request")
      }
   }

   private companion object {
      private val logger = LoggerFactory.getLogger(PooledR2dbcDataSource::class.java)
   }
}
