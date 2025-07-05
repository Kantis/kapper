# TODO
## Feature completeness
- [ ] Add `ParameterMapper` (see below)
  - [ ] Compiler plugin support for generation of parameter mappers

### Paused until requested or someone wants to build it
- Fix JS target and add support for some JS-based DB driver.

## Developer experience
- [ ] Auto-discovery of generated RowMappers
  - [ ] Similar to how KotlinX serialization detects/registers serializers during initialization
- [ ] Provide a check for `@RowMapped`-annotated class, if it already contains a `RowMapper` object (would cause compiler errors if we try to generate another)
- [ ] Generate informative errors when auto-generated RowMapper tries to bind a property of wrong type to the constructor
- [ ] Improve logging

## Testing
- [ ] Ensure transaction management works correctly
- [ ] Test integration with connection pooling, and document usage
- [ ] Add tests to ensure SQL injection is not possible
- [ ] Add tests with various other data sources (e.g., SQLite, PostgreSQL, Oracle?)

- [x] Rename `KRowMapper` -> `RowMapper`

## Support KotlinX-serialization style registration of Row/Parameter mappers
```kotlin
object BookRowMapper : RowMapper<Book> {
  // ..
}

object BookParameterMapper : ParameterMapper<Book> {
  // ..
}

@RowMapped(by = BookRowMapper::class)
// Now Mikrom should automatically figure that BookMapper should be used when dealing with queries returning Book
@ParameterMapped(by = BookParameterMapper::class)
// Now Mikrom should automatically use BookParameterMapper when executing queries with Book-parameters.
data class Book(val author: String, val title: String, val numberOfPages: Int)

```


## Parameter mappers
- Should support binding named parameters in queries.
- Generated one should automatically provide a binding for parameters by each name in the primary constructor

```kotlin
data class Employee(val name: String, val title: String, val drinksCoffee: Boolean)
val mikrom = Mikrom { }

val employees =listOf(
  Employee("Emil Kantis", "Coder", true),
  Employee("Malin Kantis", "Accountant", false),
)

// Should work, assuming we have a ParameterMapper<Employee> registered somehow.
mikrom.execute(
  Query("INSERT INTO person (name, age) VALUES (:name, 40)"),
  *employees.toTypedArray()
)
```

### Support nested structures
- When we come across a nested non-primitive, we should lookup the parameter-mapper for that type and resolve all parameters
- After resolving the nested type's available parameters, map all the names to include the name of the property
  - Example below

```kotlin
@ParameterMapped
data class Person(val name: String, val age: Int)

@ParameterMapped
data class Vehicle(val model: String, val owner: Person)

val mikrom = Mikrom { }

// Should work, assuming we have a ParameterMapper<Employee> registered somehow.
mikrom.execute(
  Query("INSERT INTO vehicle_registry (model, owner_name, owner_age) VALUES (:model, :ownerName, :ownerAge)"),
  Vehicle("Kia Ceed Sportswagon", Person("Emil Kantis", 37))
)
```
