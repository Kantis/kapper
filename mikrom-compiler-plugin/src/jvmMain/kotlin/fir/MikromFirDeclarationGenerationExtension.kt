package fir

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.extensions.predicate.DeclarationPredicate
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

public class MikromFirDeclarationGenerationExtension(
   session: FirSession,
) : FirAdditionalCheckersExtension(session) {
   public companion object {
      private val BUILDER_CLASS_NAME = Name.identifier("Builder")
      private val BUILD_FUN_NAME = Name.identifier("build")

      private val BUILDABLE_PREDICATE = DeclarationPredicate.create {
         annotated(FqName("dev.bnorm.buildable.Buildable"))
      }

      private val HAS_BUILDABLE_PREDICATE = DeclarationPredicate.create {
         hasAnnotated(FqName("dev.bnorm.buildable.Buildable"))
      }
   }
}
