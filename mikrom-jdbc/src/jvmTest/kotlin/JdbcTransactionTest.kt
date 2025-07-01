package com.github.kantis.mikrom.jdbc

import com.github.kantis.mikrom.Mikrom
import com.github.kantis.mikrom.Query
import com.github.kantis.mikrom.datasource.TransactionResult
import com.github.kantis.mikrom.execute
import com.github.kantis.mikrom.jdbc.h2.prepareH2Database
import com.github.kantis.mikrom.queryFor
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldHaveSize

data class TestRecord(val id: Int, val name: String)

class JdbcTransactionTest : FunSpec(
   {
      val mikrom = Mikrom {
         registerMapper { row ->
            TestRecord(row["id"] as Int, row["name"] as String)
         }
      }

      val dataSource = prepareH2Database(
         """
               CREATE TABLE test_records(
                 id INT PRIMARY KEY,
                 name VARCHAR(255)
               );
            """.trimIndent(),
      )

      afterEach {
         dataSource.transaction {
            mikrom.execute(Query("TRUNCATE TABLE test_records"))
            TransactionResult.Commit
         }
      }

      test("transaction commits data when returning TransactionResult.Commit") {
         // Insert data and commit
         dataSource.transaction {
            mikrom.execute(
               Query("INSERT INTO test_records (id, name) VALUES (?, ?)"),
               listOf(1, "committed_name"),
            )
            TransactionResult.Commit
         }

         // Verify data persists after commit
         dataSource.transaction {
            val records = mikrom.queryFor<TestRecord>(Query("SELECT * FROM test_records"))
            records shouldHaveSize 1
            records[0] shouldBe TestRecord(1, "committed_name")
            TransactionResult.Commit
         }
      }

      test("transaction rolls back data when returning TransactionResult.Rollback") {
         // Insert initial data and commit
         dataSource.transaction {
            mikrom.execute(
               Query("INSERT INTO test_records (id, name) VALUES (?, ?)"),
               listOf(1, "initial_name"),
            )
            TransactionResult.Commit
         }

         // Insert more data but rollback
         dataSource.transaction {
            mikrom.execute(
               Query("INSERT INTO test_records (id, name) VALUES (?, ?)"),
               listOf(2, "rolled_back_name"),
            )
            TransactionResult.Rollback
         }

         // Verify only initial data remains
         dataSource.transaction {
            val records = mikrom.queryFor<TestRecord>(Query("SELECT * FROM test_records ORDER BY id"))
            records.shouldContainExactly(TestRecord(1, "initial_name"))
            TransactionResult.Rollback
         }
      }

      test("transaction rolls back when exception is thrown") {
         // Insert initial data and commit
         dataSource.transaction {
            mikrom.execute(
               Query("INSERT INTO test_records (id, name) VALUES (?, ?)"),
               listOf(1, "initial_name"),
            )
            TransactionResult.Commit
         }

         // Attempt transaction that throws exception
         try {
            dataSource.transaction {
               mikrom.execute(
                  Query("INSERT INTO test_records (id, name) VALUES (?, ?)"),
                  listOf(2, "exception_name"),
               )
               throw RuntimeException("Simulated error")
            }
         } catch (e: RuntimeException) {
            // Expected exception
         }

         // Verify data was rolled back due to exception
         dataSource.transaction {
            val records = mikrom.queryFor<TestRecord>(Query("SELECT * FROM test_records ORDER BY id"))
            records shouldHaveSize 1
            records[0] shouldBe TestRecord(1, "initial_name")
            TransactionResult.Commit
         }
      }

      test("transaction can update existing data and commit") {
         // Insert initial data
         dataSource.transaction {
            mikrom.execute(
               Query("INSERT INTO test_records (id, name) VALUES (?, ?)"),
               TestRecord(1, "original_name"),
            )
            TransactionResult.Commit
         }

         // Update data and commit
         dataSource.transaction {
            mikrom.execute(
               Query("UPDATE test_records SET name = ? WHERE id = ?"),
               listOf("updated_name", 1),
            )
            TransactionResult.Commit
         }

         // Verify update was committed
         dataSource.transaction {
            val records = mikrom.queryFor<TestRecord>(Query("SELECT * FROM test_records"))
            records shouldHaveSize 1
            records[0] shouldBe TestRecord(1, "updated_name")
            TransactionResult.Commit
         }
      }

      test("transaction can update existing data and rollback") {
         // Insert initial data
         dataSource.transaction {
            mikrom.execute(
               Query("INSERT INTO test_records (id, name) VALUES (?, ?)"),
               listOf(1, "original_name"),
            )
            TransactionResult.Commit
         }

         // Update data but rollback
         dataSource.transaction {
            mikrom.execute(
               Query("UPDATE test_records SET name = ? WHERE id = ?"),
               listOf("updated_name", 1),
            )
            TransactionResult.Rollback
         }

         // Verify update was rolled back
         dataSource.transaction {
            val records = mikrom.queryFor<TestRecord>(Query("SELECT * FROM test_records"))
            records shouldHaveSize 1
            records[0] shouldBe TestRecord(1, "original_name")
            TransactionResult.Commit
         }
      }
   },
)
