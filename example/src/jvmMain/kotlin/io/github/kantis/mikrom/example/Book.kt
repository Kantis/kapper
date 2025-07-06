import io.github.kantis.mikrom.generator.RowMapped

@RowMapped
public data class Book(val author: String, val title: String, val numberOfPages: Int)

public fun main() {
   val book = Book.RowMapper.mapRow(
      mapOf(
         "author" to "JRR Tolkien",
         "title" to "The Fellowship of Mikrom",
         "numberOfPages" to 201,
      ),
   )
   println(book.toString())
}
