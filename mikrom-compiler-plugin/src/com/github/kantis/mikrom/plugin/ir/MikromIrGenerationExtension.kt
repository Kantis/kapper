package com.github.kantis.mikrom.plugin.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.acceptChildrenVoid

public class MikromIrGenerationExtension : IrGenerationExtension {
   override fun generate(
       moduleFragment: IrModuleFragment,
       pluginContext: IrPluginContext,
   ) {
      val visitor = MikromIrVisitor(pluginContext)
      moduleFragment.acceptChildrenVoid(visitor)
   }
}
