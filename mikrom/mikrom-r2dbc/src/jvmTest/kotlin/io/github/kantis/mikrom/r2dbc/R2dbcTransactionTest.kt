package io.github.kantis.mikrom.r2dbc

import io.github.kantis.mikrom.Mikrom
import io.github.kantis.mikrom.Query
import io.github.kantis.mikrom.datasource.Rollback
import io.github.kantis.mikrom.execute
import io.github.kantis.mikrom.queryFor
import io.github.kantis.mikrom.r2dbc.helpers.preparePostgresDatabase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList

private data class TestRecord(val id: Int, val name: String)

class R2dbcTransactionTest : FunSpec(
   {
      val mikrom = Mikrom {
         registerRowMapper { row ->
            TestRecord(
               row["id"] as Int,
               row["name"] as String,
            )
         }
      }

      lateinit var dataSource: PooledR2dbcDataSource

      beforeSpec {
         dataSource = preparePostgresDatabase(
            """
                CREATE TABLE test_records (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255)
                )
            """.trimIndent(),
         )
      }

      beforeEach {
         dataSource.suspendingTransaction {
            mikrom.execute(Query("TRUNCATE TABLE test_records"))
         }
      }

      test("transaction commits successfully") {
         dataSource.suspendingTransaction {
            mikrom.execute(
               Query("INSERT INTO test_records (name) VALUES ($1)"),
               flowOf(listOf("test record")),
            )
         }

         // Verify data persisted after transaction
         dataSource.suspendingTransaction {
            val records = mikrom.queryFor<TestRecord>(Query("SELECT * FROM test_records")).toList()
            records.size shouldBe 1
            records[0].name shouldBe "test record"
         }
      }

      test("transaction rolls back on explicit rollback") {
         val result = dataSource.suspendingTransaction {
            mikrom.execute(
               Query("INSERT INTO test_records (name) VALUES ($1)"),
               flowOf(listOf("record to rollback")),
            ).join()

            Rollback
         }

         result shouldBe Rollback

         // Verify data was not persisted
         dataSource.suspendingTransaction {
            val records = mikrom.queryFor<TestRecord>(Query("SELECT * FROM test_records")).toList()
            records.shouldContainExactly()
         }
      }

      test("transaction rolls back on exception") {
         try {
            dataSource.suspendingTransaction {
               mikrom.execute(
                  Query("INSERT INTO test_records (name) VALUES ($1)"),
                  flowOf(listOf("test record")),
               ).join()

               throw RuntimeException("Test exception")
            }
         } catch (e: RuntimeException) {
            e.message shouldBe "Test exception"
         }

         // Verify data was not persisted due to rollback
         dataSource.suspendingTransaction {
            val records = mikrom.queryFor<TestRecord>(Query("SELECT * FROM test_records")).toList()
            records.size shouldBe 0
         }
      }
   },
)
