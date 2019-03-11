package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;

public interface ASTModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
	extends ASTBuiltinTypeModel<BUILTINTYPE>,
			ASTLibraryTypeModel<LIBRARYTYPE> {

	int getNumMethods(COMPLEXTYPE complextype);
	
	void iterateClassMethods(COMPLEXTYPE complexType, ASTMethodVisitor visitor);
	
	ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> makeResolvedTypeDependency(
			TypeName completeName,
			ReferenceType referenceType,
			TypeResolveMode typeResolveMode,
			TypeVariant typeVariant,
			CompiledTypeDependency compiledTypeDependency);
}
