package com.github.kantis.kapper

import com.github.kantis.kapper.util.InMemoryDataSource
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

data class Foo(val bar: String)

class KapperTest : FunSpec({
   test("Resolve mapper") {
      val kapper =
         Kapper {
            registerMapper { row -> Foo(row["bar"] as String) }
         }

      kapper
         .resolveMapper<Foo>()
         .mapRow(mapOf("bar" to "baz")) shouldBe Foo("baz")
   }

   test("integrate with data sources") {
      val kapper =
         Kapper {
            registerMapper { row -> Foo(row["bar"] as String) }
         }

      val dataSource =
         InMemoryDataSource(
            listOf(
               mapOf("bar" to "baz"),
               mapOf("bar" to "qux"),
            ),
         )

      dataSource.transaction {
         kapper.queryFor<Foo>(Query("SELECT * FROM foo")) shouldBe
            listOf(
               Foo("baz"),
               Foo("qux"),
            )
      }
   }
})
