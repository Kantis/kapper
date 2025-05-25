package ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment

public class MikromIrGenerationExtension : IrGenerationExtension {
   override fun generate(
      moduleFragment: IrModuleFragment,
      pluginContext: IrPluginContext,
   ) {
      TODO("Not yet implemented")
   }
}
