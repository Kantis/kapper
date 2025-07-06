package io.github.kantis.mikrom.plugin.fir

import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.declarations.FirClassLikeDeclaration
import org.jetbrains.kotlin.fir.declarations.getDeprecationsProvider
import org.jetbrains.kotlin.fir.deserialization.toQualifiedPropertyAccessExpression
import org.jetbrains.kotlin.fir.expressions.FirAnnotation
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotation
import org.jetbrains.kotlin.fir.expressions.builder.buildAnnotationArgumentMapping
import org.jetbrains.kotlin.fir.expressions.builder.buildEnumEntryDeserializedAccessExpression
import org.jetbrains.kotlin.fir.expressions.builder.buildLiteralExpression
import org.jetbrains.kotlin.fir.resolve.defaultType
import org.jetbrains.kotlin.fir.resolve.providers.symbolProvider
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.toFirResolvedTypeRef
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.StandardClassIds
import org.jetbrains.kotlin.types.ConstantValueKind

internal fun FirClassLikeDeclaration.markAsDeprecatedHidden(session: FirSession) {
   replaceAnnotations(annotations + listOf(createDeprecatedHiddenAnnotation(session)))
   replaceDeprecationsProvider(this.getDeprecationsProvider(session))
}

private fun createDeprecatedHiddenAnnotation(session: FirSession): FirAnnotation =
   buildAnnotation {
      val deprecatedAnno =
         session.symbolProvider.getClassLikeSymbolByClassId(StandardClassIds.Annotations.Deprecated)
            as FirRegularClassSymbol

      annotationTypeRef = deprecatedAnno.defaultType().toFirResolvedTypeRef()

      argumentMapping = buildAnnotationArgumentMapping {
         mapping[Name.identifier("message")] =
            buildLiteralExpression(
               null,
               ConstantValueKind.String,
               "This synthesized declaration should not be used directly",
               setType = true,
            )

         // It has nothing to do with enums deserialization, but it is simply easier to build it this
         // way.
         mapping[Name.identifier("level")] =
            buildEnumEntryDeserializedAccessExpression {
               enumClassId = StandardClassIds.DeprecationLevel
               enumEntryName = Name.identifier("HIDDEN")
            }.toQualifiedPropertyAccessExpression(session)
      }
   }
