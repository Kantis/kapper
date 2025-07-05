package com.github.kantis.mikrom.jdbc

import com.github.kantis.mikrom.Mikrom
import com.github.kantis.mikrom.Query
import com.github.kantis.mikrom.datasource.TransactionResult
import com.github.kantis.mikrom.execute
import com.github.kantis.mikrom.queryFor
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.JdbcDatabaseContainerExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
import org.testcontainers.containers.PostgreSQLContainer

class MikromJdbcPostgresTest : FunSpec(
   {
      val dbContainer = PostgreSQLContainer("postgres:16-alpine")
      val postgres = install(JdbcDatabaseContainerExtension(dbContainer))
      val dataSource = JdbcDataSource(postgres)

      val mikrom = Mikrom {
         registerRowMapper { row ->
            Book(
               row["author"] as String,
               row["title"] as String,
               row["number_of_pages"] as Int,
            )
         }
      }

      dataSource.transaction {
         mikrom.execute(
            Query(
               """
                  CREATE TABLE books (
                     author VARCHAR(255),
                     title VARCHAR(255),
                     number_of_pages INT
                  )
               """.trimIndent(),
            ),
         )
         TransactionResult.Commit
      }

      afterEach {
         dataSource.transaction {
            mikrom.execute(Query("TRUNCATE TABLE books"))
            TransactionResult.Commit
         }
      }

      test("Should not allow SQL injection") {
         dataSource.transaction {
            mikrom.execute(
               Query("INSERT INTO books (author, title, number_of_pages) VALUES (?, ?, ?)"),
               listOf("JRR Tolkien", "The Hobbit", 310),
               listOf("George Orwell", "1984", 328),
            )

            TransactionResult.Commit
         }

         dataSource.transaction {
            mikrom.queryFor<Book>(
               Query("SELECT * FROM books WHERE author = ?"),
               listOf("JRR Tolkien; TRUNCATE TABLE books;"),
            )

            TransactionResult.Commit
         }

         dataSource.transaction {
            mikrom.queryFor<Book>(Query("SELECT * FROM books")).shouldNotBeEmpty()
            TransactionResult.Commit
         }
      }

      test("Should work with Postgres") {
         dataSource.transaction {
            mikrom.execute(
               Query("INSERT INTO books (author, title, number_of_pages) VALUES (?, ?, ?)"),
               listOf("JRR Tolkien", "The Hobbit", 310),
               listOf("George Orwell", "1984", 328),
            )

            mikrom.queryFor<Book>(Query("SELECT * FROM books")) shouldBe
               listOf(
                  Book("JRR Tolkien", "The Hobbit", 310),
                  Book("George Orwell", "1984", 328),
               )

            mikrom.queryFor<Book>(Query("SELECT * FROM books WHERE number_of_pages > ?"), 320) shouldBe
               listOf(Book("George Orwell", "1984", 328))

            TransactionResult.Rollback
         }

         dataSource.transaction {
            mikrom.queryFor<Book>(Query("SELECT * FROM books")).shouldBeEmpty()
            TransactionResult.Commit
         }
      }
   },
) {
   private data class Book(val author: String, val title: String, val numberOfPages: Int)
}
