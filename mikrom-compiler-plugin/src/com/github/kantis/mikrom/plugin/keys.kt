package com.github.kantis.mikrom.plugin

import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.types.ConeClassLikeType

data object MikromGenerateRowMapperKey : GeneratedDeclarationKey()

data class MikromGenerateRowMapperClassKey(
   val ownerClassSymbol: FirClassSymbol<*>,
   val rowMapperType: ConeClassLikeType
) : GeneratedDeclarationKey()
