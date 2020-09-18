package com.neaterbits.compiler.model.common;

import com.neaterbits.compiler.util.TypeResolveMode;

public interface ResolveTypesModel<COMPILATION_UNIT> extends ParseTreeModel<COMPILATION_UNIT> {

	void updateOnResolve(COMPILATION_UNIT compilationUnit, UpdateOnResolve mode, int elementParseTreeRef, UserDefinedTypeRef type, TypeResolveMode typeResolveMode);

	void replaceWithUserDefinedType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, UserDefinedTypeRef complexType);
	
	void replaceWithBuiltinType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, BuiltinTypeRef builtinType);

	void replaceWithLibraryType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, LibraryTypeRef libraryType);
	
	
}