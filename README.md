## What is Kapper?
Kapper is inspired by [Dapper](https://github.com/DapperLib/Dapper), a popular micro ORM for .NET.

## Philosophy
- No automated tracking of changes
- No reflection-based mapping
- Provide convenient DSLs, so the explicitness doesn't become tedious

I want to provide a simple way of working with databases, that does _not_ involve automatic change tracking in "entities".
I want explicit control over what is being updated, and how. With Kapper, you are in control and can write SQL that fits your exact needs.

Command-query separation becomes natural, since there is no relationship between reading and writing data tied to some "entity" class.

## Usage

### JDBC
Create a JDBC connection and construct a `Kapper` instance:

```kotlin
val dbConnection = DriverManager.getConnection("jdbc:your_database_url")
val jdbcDataSource = JdbcDataSource(dbConnection)

val kapper = Kapper {
  registerMapper { row -> Foo(row["bar"] as String) } // Register a mapper for the Foo class
}

// All queries must be executed within a transaction scope.
jdbcDataSource.transaction {
  // Execute a query

  // TODO: Support parameter binding
  val foos = kapper.queryFor<Foo>(Query("SELECT * FROM foo WHERE bar LIKE 'ba%'"))
  // foos = listOf(Foo("baz"))
}
```
