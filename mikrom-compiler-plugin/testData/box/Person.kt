// FIR_DUMP
// DUMP_IR

import com.github.kantis.mikrom.generator.GenerateRowMapper
import com.github.kantis.mikrom.KRowMapper
import kotlin.test.*

fun box(): String {
   val person = Person.RowMapper.mapRow(mapOf(
      "name" to "Brian",
      "nickname" to "bnorm",
      "age" to -1,
   ))

   assertEquals(person, Person("Brian", "bnorm", -1))
   return "OK"
}

@GenerateRowMapper
data class Person(
   val name: String,
   val nickname: String? = name,
   val age: Int = 0,
)
