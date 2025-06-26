import com.github.kantis.mikrom.generator.RowMapped

@RowMapped
data class Person(
   val name: String,
   val nickname: String? = name,
   val age: Int = 0,
)
