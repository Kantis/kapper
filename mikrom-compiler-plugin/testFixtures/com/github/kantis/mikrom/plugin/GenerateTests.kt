package com.github.kantis.mikrom.plugin

import org.jetbrains.kotlin.generators.generateTestGroupSuiteWithJUnit5

fun main() {
   generateTestGroupSuiteWithJUnit5 {
      testGroup(
         testDataRoot = "mikrom-compiler-plugin/testData",
         testsRoot = "mikrom-compiler-plugin/test-gen",
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
