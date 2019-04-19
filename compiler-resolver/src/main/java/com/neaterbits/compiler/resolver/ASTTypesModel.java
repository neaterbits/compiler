package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;

public interface ASTTypesModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
	extends ASTBuiltinTypeModel<BUILTINTYPE>,
			ASTLibraryTypeModel<LIBRARYTYPE> {

	int getNumMethods(COMPLEXTYPE complextype);
	
	void iterateClassMembers(COMPLEXTYPE complexType, ASTFieldVisitor fieldVisitor, ASTMethodVisitor methodVisitor);
	
	ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> makeResolvedTypeDependency(
			TypeName completeName,
			ReferenceType referenceType,
			TypeResolveMode typeResolveMode,
			TypeVariant typeVariant,
			CompiledTypeDependency compiledTypeDependency);
}
