package com.github.kantis.mikrom

import com.github.kantis.mikrom.util.InMemoryDataSource
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MikromTest : FunSpec({
   data class Foo(val bar: String)

   test("Resolve mapper") {
      val mikrom =
         Mikrom {
            registerRowMapper { row -> Foo(row["bar"] as String) }
         }

      mikrom
         .resolveRowMapper<Foo>()
         .mapRow(mapOf("bar" to "baz")) shouldBe Foo("baz")
   }

   test("integrate with data sources") {
      val mikrom =
         Mikrom {
            registerRowMapper { row -> Foo(row["bar"] as String) }
         }

      val dataSource =
         InMemoryDataSource(
            listOf(
               mapOf("bar" to "baz"),
               mapOf("bar" to "qux"),
            ),
         )

      dataSource.transaction {
         mikrom.queryFor<Foo>(Query("SELECT * FROM foo")) shouldBe
            listOf(
               Foo("baz"),
               Foo("qux"),
            )
      }
   }
})
