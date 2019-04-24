package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.util.TypeResolveMode;

public interface ASTTypesModel<COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
	extends ASTBuiltinTypeModel<BUILTINTYPE>,
			ASTLibraryTypeModel<LIBRARYTYPE> {

	int getNumMethods(COMPLEXTYPE complextype);
	
	void iterateClassMembers(COMPLEXTYPE complexType, ASTFieldVisitor fieldVisitor, ASTMethodVisitor methodVisitor);
	
	void updateOnResolve(COMPILATION_UNIT compilationUnit, UpdateOnResolve mode, int elementParseTreeRef, COMPLEXTYPE type, TypeResolveMode typeResolveMode);

	void replaceWithComplexType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, COMPLEXTYPE complexType);
	
	void replaceWithBuiltinType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, BUILTINTYPE builtinType);

	void replaceWithLibraryType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, LIBRARYTYPE libraryType);
	
	
}
