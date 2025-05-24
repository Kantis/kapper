package com.github.kantis.kapper.jdbc

import com.github.kantis.kapper.Kapper
import com.github.kantis.kapper.Query
import com.github.kantis.kapper.jdbc.h2.H2Helpers
import com.github.kantis.kapper.queryFor
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

data class Foo(val bar: String)

class KapperJdbcTest : FunSpec(
   {
      test("integrate with H2 JDBC data source") {
         val kapper =
            Kapper {
               registerMapper { row -> Foo(row["bar"] as String) }
            }

         val dataSource = H2Helpers.prepareDatabase(
            "jdbc:h2:mem:test;IGNORECASE=true;MODE=MYSQL;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;",
            "CREATE TABLE foo (bar VARCHAR(255))",
            "INSERT INTO foo (bar) VALUES ('baz')",
            "INSERT INTO foo (bar) VALUES ('qux')",
         )

         dataSource.transaction {
            kapper.queryFor<Foo>(Query("SELECT * FROM foo")) shouldBe
               listOf(
                  Foo("baz"),
                  Foo("qux"),
               )

            kapper.queryFor<Foo>(Query("SELECT * FROM foo WHERE bar LIKE 'ba%'")) shouldBe
               listOf(Foo("baz"))
         }
      }
   },
)
