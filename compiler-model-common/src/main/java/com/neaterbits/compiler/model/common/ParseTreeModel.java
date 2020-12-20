package com.neaterbits.compiler.model.common;

import java.io.PrintStream;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.util.parse.ScopesListener;

public interface ParseTreeModel<COMPILATION_UNIT> {

	void iterateScopesAndVariables(COMPILATION_UNIT compilationUnit, ScopesListener scopesListener);

	String getMethodName(COMPILATION_UNIT compilationUnit, int parseTreeMethodDeclarationRef);

	String getVariableName(COMPILATION_UNIT compilationUnit, int parseTreeVariableDeclarationRef);

	String getClassDataFieldMemberName(COMPILATION_UNIT compilationUnit, int parseTreeDataMemberDeclarationRef);

	String getClassName(COMPILATION_UNIT compilationUnit, int parseTreeTypeDeclarationRef);

	void print(COMPILATION_UNIT compilationUnit, PrintStream out);

	int getNumMethods(COMPILATION_UNIT compilationUnit, UserDefinedTypeRef complextype);

    void iterateTypes(COMPILATION_UNIT compilationUnit, TypeVisitor visitor);

    void iterateClassMembers(COMPILATION_UNIT compilationUnit, UserDefinedTypeRef complexType, FieldVisitor fieldVisitor, MethodVisitor methodVisitor);

    void replaceTypeReference(COMPILATION_UNIT compilationUnit, int toReplace, int typeNo, TypeName typeName);

    void iterateTypeReferences(COMPILATION_UNIT compilationUnit, TypeReferenceVisitor<COMPILATION_UNIT> visitor);
}
