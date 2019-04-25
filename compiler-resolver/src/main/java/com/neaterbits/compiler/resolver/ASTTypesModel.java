package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.util.TypeResolveMode;
import com.neaterbits.compiler.util.model.BuiltinTypeRef;
import com.neaterbits.compiler.util.model.LibraryTypeRef;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;

public interface ASTTypesModel<COMPILATION_UNIT> {

	int getNumMethods(COMPILATION_UNIT compilationUnit, UserDefinedTypeRef complextype);
	
	void iterateClassMembers(COMPILATION_UNIT compilationUnit, UserDefinedTypeRef complexType, ASTFieldVisitor fieldVisitor, ASTMethodVisitor methodVisitor);
	
	void updateOnResolve(COMPILATION_UNIT compilationUnit, UpdateOnResolve mode, int elementParseTreeRef, UserDefinedTypeRef type, TypeResolveMode typeResolveMode);

	void replaceWithUserDefinedType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, UserDefinedTypeRef complexType);
	
	void replaceWithBuiltinType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, BuiltinTypeRef builtinType);

	void replaceWithLibraryType(COMPILATION_UNIT compilationUnit, int typeReferenceParseTreeRef, LibraryTypeRef libraryType);
	
	
}
