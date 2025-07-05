package com.github.kantis.mikrom.jdbc

import com.github.kantis.mikrom.Mikrom
import com.github.kantis.mikrom.Query
import com.github.kantis.mikrom.execute
import com.github.kantis.mikrom.jdbc.h2.prepareH2Database
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class DataTypesTest: FunSpec({
   val dataSource = prepareH2Database(
      """
         CREATE TABLE foo(
           id INT PRIMARY KEY AUTO_INCREMENT,
           bar VARCHAR(255)
         );
      """.trimIndent(),
   )

   val mikrom = Mikrom { }

   test("Mixing null as a parameter should work") {
      dataSource.transaction {
         mikrom.execute(
            Query("INSERT INTO foo (bar) VALUES (?)"),
            listOf("baz"),
            listOf(null),
         )

         val result = query(Query("SELECT * FROM foo"))

         result shouldBe listOf(
            mapOf("id" to 1, "bar" to "baz"),
            mapOf("id" to 2, "bar" to null),
         )
      }
    }
})
