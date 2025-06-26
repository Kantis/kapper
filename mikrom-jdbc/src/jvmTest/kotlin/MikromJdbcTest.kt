package com.github.kantis.mikrom.jdbc

import com.github.kantis.mikrom.Mikrom
import com.github.kantis.mikrom.Query
import com.github.kantis.mikrom.datasource.TransactionResult
import com.github.kantis.mikrom.execute
import com.github.kantis.mikrom.jdbc.h2.H2Helpers
import com.github.kantis.mikrom.queryFor
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

data class Book(val author: String, val title: String, val numberOfPages: Int)

class MikromJdbcTest : FunSpec(
   {
      test("integrate with H2 JDBC data source") {
         val mikrom =
            Mikrom {
               registerMapper { row -> Book(row["author"] as String, row["title"] as String, row["number_of_pages"] as Int) }
            }

         val dataSource = H2Helpers.prepareDatabase(
            "jdbc:h2:mem:test;IGNORECASE=true;MODE=MYSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;",
            """
               CREATE TABLE books (
                  author VARCHAR(255),
                  title VARCHAR(255),
                  number_of_pages INT
               )
            """.trimIndent(),
         )

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
      }
   },
)
