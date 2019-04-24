package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;

public interface ASTTypesModel<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
	extends ASTBuiltinTypeModel<BUILTINTYPE>,
			ASTLibraryTypeModel<LIBRARYTYPE> {

	int getNumMethods(COMPLEXTYPE complextype);
	
	void iterateClassMembers(COMPLEXTYPE complexType, ASTFieldVisitor fieldVisitor, ASTMethodVisitor methodVisitor);
	
	ResolvedTypeDependency makeResolvedTypeDependency(
			TypeName completeName,
			ReferenceType referenceType,
			TypeResolveMode typeResolveMode,
			TypeVariant typeVariant,
			CompiledTypeDependency compiledTypeDependency);

	void updateOnResolve(COMPILATION_UNIT compilationUnit, UpdateOnResolve mode, int elementParseTreeRef, COMPLEXTYPE type, TypeResolveMode typeResolveMode);

	void replaceWithComplexType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, COMPLEXTYPE complexType);
	
	void replaceWithBuiltinType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, BUILTINTYPE builtinType);

	void replaceWithLibraryType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, LIBRARYTYPE libraryType);
	
	
}
