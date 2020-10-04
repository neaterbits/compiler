package com.neaterbits.compiler.model.common;

import java.io.PrintStream;

import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.parse.ScopesListener;

public interface ParseTreeModel<COMPILATION_UNIT> {

	void iterateScopesAndVariables(COMPILATION_UNIT sourceFile, ScopesListener scopesListener);

	String getMethodName(COMPILATION_UNIT sourceFile, int parseTreeMethodDeclarationRef);

	String getVariableName(COMPILATION_UNIT sourceFile, int parseTreeVariableDeclarationRef);

	String getClassDataFieldMemberName(COMPILATION_UNIT sourceFile, int parseTreeDataMemberDeclarationRef);

	String getClassName(COMPILATION_UNIT sourceFile, int parseTreeTypeDeclarationRef);

	void print(COMPILATION_UNIT sourceFile, PrintStream out);

	int getNumMethods(COMPILATION_UNIT compilationUnit, UserDefinedTypeRef complextype);

    void iterateTypes(COMPILATION_UNIT compilationUnit, TypeVisitor visitor);

    void iterateClassMembers(COMPILATION_UNIT compilationUnit, UserDefinedTypeRef complexType, FieldVisitor fieldVisitor, MethodVisitor methodVisitor);

    void replaceTypeReference(COMPILATION_UNIT compilationUnit, int toReplace, int typeNo, TypeName typeName);

    void iterateTypeReferences(COMPILATION_UNIT compilationUnit, TypeReferenceVisitor<COMPILATION_UNIT> visitor);
}
