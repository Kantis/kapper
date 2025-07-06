package io.github.kantis.mikrom.plugin.fir

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

public class MikromFirExtensionRegistrar : FirExtensionRegistrar() {
   override fun ExtensionRegistrarContext.configurePlugin() {
      +::MikromFirDeclarationGenerationExtension
   }
}
