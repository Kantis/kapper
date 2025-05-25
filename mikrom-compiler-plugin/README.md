## Goals of the compiler plugin
- Create a global registry for auto-generated RowMappers for Mikrom to use
- Find classes annotated with `@GenerateRowMapper`
  - Generate a `RowMapper`, including all the fields in the primary constructor
  - Support some `@ColumnName` annotation
- Register the generated RowMapper in the registry

Do something similar to generate `ParamMapper`s

Check out the following repositories for inspiration:
- https://github.com/bnorm/buildable
- https://github.com/demiurg906/kotlin-compiler-plugin-template
- https://github.com/cashapp/burst
- https://github.com/drewhamilton/poko
- https://github.com/ZacSweers/metro
