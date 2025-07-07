package io.github.kantis.mikrom.r2dbc

import io.github.kantis.mikrom.Mikrom
import io.github.kantis.mikrom.Query
import io.github.kantis.mikrom.execute
import io.github.kantis.mikrom.queryFor
import io.github.kantis.mikrom.r2dbc.helpers.preparePostgresDatabase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.job

private data class Book(val author: String, val title: String, val numberOfPages: Int)

class MikromR2dbcTest : FunSpec(
   {
      test("integrate with R2DBC PostgreSQL data source") {
         val mikrom = Mikrom {
            registerRowMapper { row ->
               Book(
                  row["author"] as String,
                  row["title"] as String,
                  row["number_of_pages"] as Int,
               )
            }
         }

         val dataSource = preparePostgresDatabase(
            """
                CREATE TABLE books (
                    author VARCHAR(255),
                    title VARCHAR(255),
                    number_of_pages INT
                )
         """.trimIndent(),
         )

         dataSource.suspendingTransaction {
            mikrom.execute(
               Query("INSERT INTO books (author, title, number_of_pages) VALUES ($1, $2, $3)"),
               flowOf(
                  listOf("JRR Tolkien", "The Hobbit", 310),
                  listOf("George Orwell", "1984", 328),
               ),
            ).join()

            mikrom
               .queryFor<Book>(Query("SELECT * FROM books ORDER BY author ASC"))
               .toList()
               .shouldContainExactly(
                  Book("George Orwell", "1984", 328),
                  Book("JRR Tolkien", "The Hobbit", 310),
               )

            mikrom.queryFor<Book>(Query("SELECT * FROM books WHERE number_of_pages > $1"), listOf(320)).toList() shouldBe
               listOf(Book("George Orwell", "1984", 328))
         }
      }
   },
)
