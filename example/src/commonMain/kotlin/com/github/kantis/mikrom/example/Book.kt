import com.github.kantis.mikrom.generator.RowMapped

@RowMapped
data class Book(val author: String, val title: String, val numberOfPages: Int)

fun main(){
   Book.RowMapper.mapRow(
      mapOf(
         "author" to "JRR Tolkien",
         "title" to "The Fellowship of Mikrom",
         "numberOfPages" to 201
      )
   )
}

