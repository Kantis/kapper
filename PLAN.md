# Plan: Implement ParameterMapper Support

**Goal:** Implement the ability to automatically generate and register `ParameterMapper` instances for data classes, enabling type-safe parameter binding in SQL queries.

**Steps:**

1.  **Define `ParameterMapper` Interface:**  Ensure the `ParameterMapper` interface exists in `mikrom-core` and defines a method to map object properties to query parameters.
2.  **Compiler Plugin Integration:** Modify the `mikrom-compiler-plugin` to:
    *   Detect data classes annotated with `@ParameterMapped`.
    *   Generate a `ParameterMapper` implementation for each annotated data class.
    *   Register the generated `ParameterMapper` with the `Mikrom` instance during initialization.
3.  **`Mikrom` Registration:** Update the `Mikrom` class to accept and store a list of `ParameterMapper` instances.
4.  **Query Execution:** Modify the query execution logic to use the registered `ParameterMapper` instances to bind parameters to queries.
5.  **Testing:**
    *   Create test cases to verify that `ParameterMapper` instances are generated and registered correctly.
    *   Create test cases to verify that parameters are bound correctly to queries using the generated `ParameterMapper` instances.
    *   Test with nested data structures.

**Deliverables:**

*   Updated `mikrom-core` with the `ParameterMapper` interface.
*   Updated `mikrom-compiler-plugin` to generate and register `ParameterMapper` instances.
*   Updated `Mikrom` class to accept and store `ParameterMapper` instances.
*   Updated query execution logic to use `ParameterMapper` instances.
*   Comprehensive test suite to verify the functionality.

**Dependencies:**

*   Existing `mikrom-core` and `mikrom-compiler-plugin` code base.
*   Kotlin compiler plugin API.
*   Testing framework.
