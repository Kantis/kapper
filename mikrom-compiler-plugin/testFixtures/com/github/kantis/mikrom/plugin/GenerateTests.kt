package io.github.kantis.mikrom.plugin

import org.jetbrains.kotlin.generators.generateTestGroupSuiteWithJUnit5

fun main() {
   generateTestGroupSuiteWithJUnit5 {
      testGroup(
         testDataRoot = "testData",
         testsRoot = "test-gen",
      ) {
         testClass<AbstractBoxTest> {
            model("box")
         }

         testClass<AbstractDiagnosticTest> {
            model("diagnostic")
         }
      }
   }
}
