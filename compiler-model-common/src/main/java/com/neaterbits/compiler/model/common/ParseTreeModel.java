package com.neaterbits.compiler.model.common;

import java.io.PrintStream;
import java.util.List;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.types.Mutability;
import com.neaterbits.compiler.types.Visibility;
import com.neaterbits.compiler.util.parse.ScopesListener;

public interface ParseTreeModel<COMPILATION_UNIT> {
    
    void iterateElements(
            COMPILATION_UNIT compilationUnit,
            ElementVisitor<COMPILATION_UNIT> visitor);

	void iterateScopesAndVariables(COMPILATION_UNIT compilationUnit, ScopesListener scopesListener);
	
    List<String> getNamespace(COMPILATION_UNIT compilationUnit, int parseTreeRef);

    String getMethodName(COMPILATION_UNIT compilationUnit, int parseTreeMethodDeclarationRef);

	String getVariableName(COMPILATION_UNIT compilationUnit, int parseTreeVariableDeclarationRef);

	String getClassDataFieldMemberName(COMPILATION_UNIT compilationUnit, int parseTreeDataMemberDeclarationRef);

	String getClassName(COMPILATION_UNIT compilationUnit, int parseTreeTypeDeclarationRef);

    String getEnumName(COMPILATION_UNIT compilationUnit, int parseTreeTypeDeclarationRef);

    String getInterfaceName(COMPILATION_UNIT compilationUnit, int parseTreeTypeDeclarationRef);

	void print(COMPILATION_UNIT compilationUnit, PrintStream out);

	int getNumMethods(COMPILATION_UNIT compilationUnit, UserDefinedTypeRef complextype);

    void iterateTypesAndMembers(
            COMPILATION_UNIT compilationUnit,
            TypeMemberVisitor visitor,
            boolean fields,
            boolean methods);
    
    default void iterateTypes(COMPILATION_UNIT compilationUnit, TypeVisitor visitor) {
        
        final TypeMemberVisitor fieldVisitor = new TypeMemberVisitor() {
            
            @Override
            public void onNamespaceStart() {
                visitor.onNamespaceStart();
            }
            
            @Override
            public void onNamespacePart(CharSequence part) {
                visitor.onNamespacePart(part);
            }
            
            @Override
            public void onNamespaceEnd() {
                visitor.onNamespaceEnd();
            }
            
            @Override
            public void onInterfaceStart(CharSequence name) {
                visitor.onInterfaceStart(name);
            }
            
            @Override
            public void onInterfaceEnd() {
                visitor.onInterfaceEnd();
            }
            
            @Override
            public void onEnumStart(CharSequence name) {
                visitor.onEnumStart(name);
            }
            
            @Override
            public void onEnumEnd() {
                visitor.onEnumEnd();
            }
            
            @Override
            public void onClassStart(CharSequence name) {
                visitor.onClassStart(name);
            }
            
            @Override
            public void onClassEnd() {
                visitor.onClassEnd();
            }
            
            @Override
            public void onField(CharSequence name, TypeName type, int numArrayDimensions, boolean isStatic,
                    Visibility visibility, Mutability mutability, boolean isVolatile, boolean isTransient, int indexInType) {

                throw new UnsupportedOperationException();
            }

            @Override
            public void onMethod(String name, MethodVariant methodVariant, TypeName returnType,
                    TypeName[] parameterTypes, int indexInType) {
                
                throw new UnsupportedOperationException();
            }
        };
        
        iterateTypesAndMembers(compilationUnit, fieldVisitor, false, false);
    }

    default void iterateTypeMembers(COMPILATION_UNIT compilationUnit, TypeMemberVisitor memberVisitor) {
        
        iterateTypesAndMembers(compilationUnit, memberVisitor, true, true);
    }

    void replaceTypeReference(COMPILATION_UNIT compilationUnit, int toReplace, int typeNo, TypeName typeName);

    void iterateTypeReferences(COMPILATION_UNIT compilationUnit, TypeReferenceVisitor<COMPILATION_UNIT> visitor);
}
