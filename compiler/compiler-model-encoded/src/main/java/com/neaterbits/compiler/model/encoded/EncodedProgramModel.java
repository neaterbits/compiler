package com.neaterbits.compiler.model.encoded;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.ast.encoded.AST;
import com.neaterbits.compiler.ast.encoded.ASTBufferRead;
import com.neaterbits.compiler.ast.encoded.ASTBufferRead.ParseTreeElementRef;
import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.model.common.ElementVisitor;
import com.neaterbits.compiler.model.common.TypeMemberVisitor;
import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.compiler.model.common.ProgramModel;
import com.neaterbits.compiler.model.common.ResolvedScopesListener;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.model.common.SourceTokenUtil;
import com.neaterbits.compiler.model.common.SourceTokenUtil.ASTAccess;
import com.neaterbits.compiler.model.common.SourceTokenVisitor;
import com.neaterbits.compiler.model.common.TypeReferenceVisitor;
import com.neaterbits.compiler.model.common.UnresolvedScopesListener;
import com.neaterbits.compiler.model.common.UserDefinedTypeRef;
import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.types.Mutability;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.Visibility;
import com.neaterbits.compiler.types.imports.TypeImport;
import com.neaterbits.compiler.util.FileSpec;

public final class EncodedProgramModel
    extends EncodedImportsModel
    implements ProgramModel<EncodedParsedFile, EncodedCompilationUnit> {

    private static final ASTAccess<Integer, EncodedCompilationUnit> AST_ACCESS = new EncodedASTAccess();

    public EncodedProgramModel(List<TypeImport> implicitImports) {
        super(implicitImports);
    }

    @Override
    public void iterate(
            EncodedCompilationUnit compilationUnit,
            SourceTokenVisitor iterator,
            ResolvedTypes resolvedTypes,
            boolean visitPlaceholderElements) {

    }

    @Override
    public ISourceToken getTokenAtOffset(
            EncodedCompilationUnit compilationUnit,
            long offset,
            ResolvedTypes resolvedTypes) {

        // FIXME Auto-generated method stub
        return null;
    }

    @Override
    public ISourceToken getTokenAtParseTreeRef(
            EncodedCompilationUnit compilationUnit,
            int parseTreeRef,
            ResolvedTypes resolvedTypes) {

        return SourceTokenUtil.makeSourceToken(
                parseTreeRef,
                compilationUnit,
                resolvedTypes,
                this,
                AST_ACCESS);
    }

    @Override
    public int getTokenOffset(EncodedCompilationUnit compilationUnit, int parseTreeTokenRef) {

        return compilationUnit.getTokenOffset(parseTreeTokenRef);
    }

    @Override
    public int getTokenLength(EncodedCompilationUnit compilationUnit, int parseTreeTokenRef) {

        return compilationUnit.getTokenLength(parseTreeTokenRef);
    }

    @Override
    public String getTokenString(EncodedCompilationUnit compilationUnit, int parseTreeTokenRef) {

        return compilationUnit.getTokenString(parseTreeTokenRef);
    }

    @Override
    public void iterateUnresolvedScopesAndVariables(
            EncodedCompilationUnit compilationUnit,
            UnresolvedScopesListener scopesListener) {

        final ASTBufferRead astBuffer = compilationUnit.getBuffer();

        final ParseTreeElementRef ref = new ParseTreeElementRef();

        final ParseTreeElementRef nextRef = new ParseTreeElementRef();

        int parseTreeRef = 0;

        boolean done = false;

        do {
            astBuffer.getParseTreeElement(parseTreeRef, ref);
            
            switch (ref.element) {
            case NAMESPACE:
                if (ref.isStart) {
                    scopesListener.onNamespaceStart();
                }
                else {
                    scopesListener.onNamespaceEnd();
                }
                break;
                
            case VARIABLE_DECLARATION_STATEMENT:
                if (ref.isStart) {
                    final int typeElementParseTreeRef
                        = parseTreeRef + AST.sizeStart(ref, astBuffer);

                    astBuffer.getParseTreeElement(typeElementParseTreeRef, nextRef);
                
                    final int typeNo = AST.decodeResolvedTypeReferenceTypeNo(astBuffer, nextRef.index);
                    
                    scopesListener.onScopeVariableDeclarationStatementStart(parseTreeRef, typeNo, typeElementParseTreeRef);
                }
                else {
                    scopesListener.onScopeVariableDeclarationStatementEnd(parseTreeRef);
                }
                break;
                
            case VARIABLE_DECLARATOR:
                if (ref.isStart) {
                    
                    final int nameElementParseTreeRef
                        = parseTreeRef + AST.sizeStart(ref, astBuffer);

                    astBuffer.getParseTreeElement(nameElementParseTreeRef, nextRef);
                    
                    final int nameRef = AST.decodeVariable(astBuffer, nextRef.index);
                    
                    scopesListener.onScopeVariableDeclarator(
                            parseTreeRef,
                            compilationUnit.getStringFromRef(nameRef));
                }
                break;
                
            case NAMESPACE_PART:
                final int namespacePartRef = AST.decodeNamespacePart(astBuffer, ref.index);
                
                scopesListener.onNamespacePart(compilationUnit.getStringFromRef(namespacePartRef));
                break;
                
            case UNRESOLVED_NAME_PRIMARY:
                
                final int nameRef = AST.decodeNamePrimaryName(astBuffer, ref.index);
                
                scopesListener.onUnresolvedNamePrimary(
                        parseTreeRef,
                        compilationUnit.getStringFromRef(nameRef));
                break;
                
            default:
                break;
            }
            
            parseTreeRef += ref.isStart
                    ? AST.sizeStart(ref.element, astBuffer, ref.index)
                    : AST.sizeEnd(ref.element);
                    
        } while (!done);
    }

    @Override
    public void iterateResolvedScopesAndVariables(EncodedCompilationUnit compilationUnit,
            ResolvedScopesListener scopesListener) {

        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceNamePrimaryWithNameReference(
            EncodedCompilationUnit compilationUnit,
            int namePrimaryParseTreeRef,
            String name) {
        
        compilationUnit.replaceNamePrimaryWithNameReference(namePrimaryParseTreeRef, name);
    }

    @Override
    public void replaceNamePrimaryWithFieldAccess(
            EncodedCompilationUnit compilationUnit,
            int namePrimaryParseTreeRef,
            int classTypeParseTreeRef,
            String name) {

        compilationUnit.replaceNamePrimaryWithFieldAccess(
                namePrimaryParseTreeRef,
                classTypeParseTreeRef,
                name);
    }

    @Override
    public void replaceNamePrimaryWithStaticReference(
            EncodedCompilationUnit compilationUnit,
            int namePrimaryParseTreeRef,
            int classTypeParseTreeRef,
            String name) {

        compilationUnit.replaceNamePrimaryWithStaticReference(
                namePrimaryParseTreeRef,
                classTypeParseTreeRef,
                name);
    }

    @Override
    public List<String> getNamespace(EncodedCompilationUnit compilationUnit, int parseTreeRef) {

        final ASTBufferRead astBuffer = compilationUnit.getBuffer();

        int curParseTreeRef = parseTreeRef + AST.sizeStart(ParseTreeElement.NAMESPACE, astBuffer, -1);

        boolean done = false;

        final List<String> parts = new ArrayList<>();
        
        do {
            if (astBuffer.getParseTreeElement(curParseTreeRef) != ParseTreeElement.NAMESPACE_PART) {
                
                if (parts.isEmpty()) {
                    throw new IllegalStateException();
                }
                
                done = true;
            }

            curParseTreeRef += AST.sizeLeaf(ParseTreeElement.NAMESPACE_PART, astBuffer, -1);

        } while (!done);
        
        return parts;
    }


    @Override
    public String getMethodName(EncodedCompilationUnit compilationUnit, int parseTreeMethodDeclarationRef) {
        // FIXME Auto-generated method stub
        return null;
    }

    @Override
    public String getVariableName(
            EncodedCompilationUnit compilationUnit,
            int parseTreeVariableDeclarationRef) {

        return compilationUnit.getStringFromASTBufferOffset(parseTreeVariableDeclarationRef + 1);
    }

    @Override
    public String getClassDataFieldMemberName(
            EncodedCompilationUnit compilationUnit,
            int parseTreeDataMemberDeclarationRef) {

        return compilationUnit.getStringFromASTBufferOffset(parseTreeDataMemberDeclarationRef + 1);
    }

    @Override
    public String getClassName(
            EncodedCompilationUnit compilationUnit,
            int parseTreeTypeDeclarationRef) {

        return compilationUnit.getStringFromRef(
                AST.decodeClassName(
                        compilationUnit.getBuffer(),
                        AST.index(parseTreeTypeDeclarationRef)));
    }

    @Override
    public String getEnumName(EncodedCompilationUnit compilationUnit, int parseTreeTypeDeclarationRef) {
        
        return compilationUnit.getStringFromRef(
                AST.decodeEnumName(
                        compilationUnit.getBuffer(),
                        AST.index(parseTreeTypeDeclarationRef)));
    }


    @Override
    public String getInterfaceName(EncodedCompilationUnit compilationUnit, int parseTreeTypeDeclarationRef) {
        return compilationUnit.getStringFromRef(
                AST.decodeInterfaceName(
                        compilationUnit.getBuffer(),
                        AST.index(parseTreeTypeDeclarationRef)));
    }

    @Override
    public void print(EncodedCompilationUnit compilationUnit, PrintStream out) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumMethods(
            EncodedCompilationUnit compilationUnit,
            UserDefinedTypeRef userDefinedType) {

        Objects.requireNonNull(compilationUnit);
        Objects.requireNonNull(userDefinedType);

        int numMethods = 0;

        if (compilationUnit.getParseTreeElement(userDefinedType.getParseTreeRef()) != ParseTreeElement.CLASS_DEFINITION) {
            throw new IllegalStateException();
        }

        return numMethods;
    }

    @Override
    public void iterateElements(EncodedCompilationUnit compilationUnit,
            ElementVisitor<EncodedCompilationUnit> visitor) {
        
        final ASTBufferRead astBuffer = compilationUnit.getBuffer();

        final ParseTreeElementRef ref = new ParseTreeElementRef();

        int parseTreeRef = 0;

        boolean done = false;

        do {
            astBuffer.getParseTreeElement(parseTreeRef, ref);
            
            if (ref.isStart) {
                visitor.onElementStart(compilationUnit, parseTreeRef, ref.element);
            }
            else {
                visitor.onElementEnd(compilationUnit, parseTreeRef, ref.element);
                
                if (ref.element == ParseTreeElement.COMPILATION_UNIT) {
                    done = true;
                }
            }
            
            parseTreeRef += ref.isStart
                    ? AST.sizeStart(ref.element, astBuffer, ref.index)
                    : AST.sizeEnd(ref.element);
                    
        } while (!done);
    }


    @Override
    public void iterateTypesAndMembers(
            EncodedCompilationUnit compilationUnit,
            TypeMemberVisitor visitor,
            boolean fields,
            boolean methods) {

        // Iterate through the parse tree
        final ASTBufferRead astBuffer = compilationUnit.getBuffer();

        final ParseTreeElementRef ref = new ParseTreeElementRef();

        int parseTreeRef = 0;

        boolean done = false;

        int fieldIndex = 0;
        int methodIndex = 0;
        
        do {
            astBuffer.getParseTreeElement(parseTreeRef, ref);
            
            boolean updateSize = true;

            switch (ref.element) {
            case NAMESPACE:
                if (ref.isStart) {
                    visitor.onNamespaceStart();
                }
                else {
                    visitor.onNamespaceEnd();

                    done = true;
                }
                break;

            case NAMESPACE_PART:
                final int partStringRef = AST.decodeNamespacePart(astBuffer, ref.index);
                visitor.onNamespacePart(compilationUnit.getStringFromRef(partStringRef));
                break;

            case CLASS_DEFINITION:
                if (ref.isStart) {
                    final int classNameStringRef = AST.decodeClassName(astBuffer, ref.index);

                    visitor.onClassStart(compilationUnit.getStringFromRef(classNameStringRef));
                }
                else {
                    fieldIndex = 0;
                    methodIndex = 0;

                    visitor.onClassEnd();
                }
                break;

            case INTERFACE_DEFINITION:
                if (ref.isStart) {
                    final int interfaceNameStringRef = AST.decodeInterfaceName(astBuffer, ref.index);

                    visitor.onInterfaceStart(compilationUnit.getStringFromRef(interfaceNameStringRef));
                }
                else {
                    fieldIndex = 0;
                    methodIndex = 0;

                    visitor.onInterfaceEnd();
                }
                break;

            case ENUM_DEFINITION:
                if (ref.isStart) {
                    final int enumNameStringRef = AST.decodeEnumName(astBuffer, ref.index);

                    visitor.onEnumStart(compilationUnit.getStringFromRef(enumNameStringRef));
                }
                else {
                    fieldIndex = 0;
                    methodIndex = 0;

                    visitor.onEnumEnd();
                }
                break;

            case FIELD_DECLARATION:

                if (fields && ref.isStart) {

                    parseTreeRef = processField(
                            compilationUnit,
                            visitor,
                            ref,
                            astBuffer,
                            fieldIndex ++,
                            parseTreeRef + AST.sizeStart(ref, astBuffer));
                    
                    updateSize = false;
                }
                break;

            case CLASS_METHOD_MEMBER:
            case INTERFACE_METHOD_MEMBER:

                if (methods && ref.isStart) {

                    parseTreeRef = processMethod(
                                        compilationUnit,
                                        visitor,
                                        ref,
                                        astBuffer,
                                        methodIndex ++,
                                        parseTreeRef + AST.sizeStart(ref, astBuffer));

                    updateSize = false;
                }
                break;
                
            default:
                break;
            }

            if (updateSize) {
                parseTreeRef += ref.isStart
                    ? AST.sizeStart(ref, astBuffer)
                    : AST.sizeEnd(ref.element);
            }

        } while (!done);
    }
    
    private int processField(
            EncodedCompilationUnit compilationUnit,
            TypeMemberVisitor visitor,
            ParseTreeElementRef ref,
            ASTBufferRead astBuffer,
            final int indexInType,
            final int startParseTreeRef) {
        
        boolean done = false;
        
        int parseTreeRef = startParseTreeRef;
        
        String name = null;
        TypeName type = null;
        int numArrayDimensions = 0;
        boolean isStatic = false;
        Visibility visibility = null;
        Mutability mutability = null;
        boolean isVolatile = false;
        boolean isTransient = false;
        
        do {
            astBuffer.getParseTreeElement(parseTreeRef, ref);
            
            switch (ref.element) {
            case FIELD_MODIFIER_HOLDER:
                break;
                
            default:
                throw new IllegalStateException("Unknown element " + ref.element);
            }
            
            parseTreeRef += ref.isStart
                    ? AST.sizeStart(ref, astBuffer)
                    : AST.sizeEnd(ref.element);
            
        } while (!done);
        
        
        visitor.onField(
                name,
                type,
                numArrayDimensions,
                isStatic,
                visibility,
                mutability,
                isVolatile,
                isTransient,
                indexInType);
        
        return parseTreeRef;
    }

    private int processMethod(
            EncodedCompilationUnit compilationUnit,
            TypeMemberVisitor visitor,
            ParseTreeElementRef ref,
            ASTBufferRead astBuffer,
            final int indexInType,
            final int startParseTreeRef) {
        
        boolean done = false;
        
        int parseTreeRef = startParseTreeRef;
        
        String name = null;
        MethodVariant methodVariant = MethodVariant.OVERRIDABLE_IMPLEMENTATION;
        TypeName returnType = null;
        TypeName [] parameterTypes = null;
        
        do {
            astBuffer.getParseTreeElement(parseTreeRef, ref);
            
            switch (ref.element) {
            case CLASS_METHOD_MODIFIER_HOLDER:
                switch (AST.decodeClassMethodModifierType(astBuffer, ref.index)) {
                case STATIC:
                    methodVariant = MethodVariant.STATIC;
                    break;
                    
                case OVERRIDE:
                    switch (AST.decodeClassMethodOverride(astBuffer, ref.index)) {
                    case ABSTRACT:
                        methodVariant = MethodVariant.ABSTRACT;
                        break;
                        
                    case FINAL:
                        methodVariant = MethodVariant.FINAL_IMPLEMENTATION;
                        break;

                    default:
                        throw new IllegalStateException();
                    }
                    break;
                    
                case VISIBILITY:
                    break;
                    
                default:
                    throw new IllegalStateException();
                }
                break;
                
            default:
                throw new IllegalStateException("Unknown element " + ref.element);
            }
            
            parseTreeRef += ref.isStart
                    ? AST.sizeStart(ref, astBuffer)
                    : AST.sizeEnd(ref.element);
            
        } while (!done);
        
        visitor.onMethod(
                name,
                methodVariant,
                returnType,
                parameterTypes,
                indexInType);
        
        return parseTreeRef;
    }
    
    @Override
    public EncodedParsedFile getParsedFile(Collection<EncodedParsedFile> module, FileSpec path) {

        return module.stream()
                .filter(pf -> pf.getFileSpec().equals(path))
                .findFirst()
                .orElse(null);
    }

    @Override
    public EncodedCompilationUnit getCompilationUnit(EncodedParsedFile compilationUnit) {

        return compilationUnit.getCompilationUnit();
    }


    @Override
    public void replaceTypeReference(EncodedCompilationUnit compilationUnit, int toReplace, int typeNo, TypeName typeName) {

        compilationUnit.replaceTypeReference(toReplace, typeNo);
    }


    @Override
    public void iterateTypeReferences(
            EncodedCompilationUnit compilationUnit,
            TypeReferenceVisitor<EncodedCompilationUnit> visitor) {

        iterateTypeReferences(compilationUnit.getBuffer(), 0, compilationUnit, visitor);
    }

    private static void iterateTypeReferences(
            ASTBufferRead astBuffer,
            int parseTreeRef,
            EncodedCompilationUnit compilationUnit,
            TypeReferenceVisitor<EncodedCompilationUnit> visitor) {

        final ParseTreeElementRef ref = new ParseTreeElementRef();

        boolean done = false;

        do {
            astBuffer.getParseTreeElement(parseTreeRef, ref);

            int nextParseTreeRef = parseTreeRef + (ref.isStart
                    ? AST.sizeStart(ref.element, astBuffer, ref.index)
                    : AST.sizeEnd(ref.element));

            switch (ref.element) {

            case NAMESPACE:
                if (ref.isStart) {
                    visitor.onNamespaceStart();
                }
                else {
                    visitor.onNamespaceEnd();

                    done = true;
                }
                break;

            case NAMESPACE_PART:
                final int partStringRef = AST.decodeNamespacePart(astBuffer, ref.index);
                visitor.onNamespacePart(compilationUnit.getStringFromRef(partStringRef));
                break;

            case UNRESOLVED_IDENTIFIER_TYPE_REFERENCE:
                if (ref.isStart) {
                    final int typeReferenceName
                        = AST.decodeIdentifierTypeReferenceName(astBuffer, ref.index);

                    visitor.onNonScopedTypeReference(
                            compilationUnit,
                            parseTreeRef,
                            compilationUnit.getStringFromRef(typeReferenceName));
                }
                break;

            case RESOLVED_TYPE_REFERENCE:
                final int typeNo = AST.decodeResolvedTypeReferenceTypeNo(astBuffer, ref.index);

                visitor.onResolvedTypeReference(compilationUnit, parseTreeRef, typeNo);
                break;

            case COMPILATION_UNIT:
                if (!ref.isStart) {
                    done = true;
                }
                break;

            case REPLACE:
                final int replacementIndex = astBuffer.getReplacementIndex(parseTreeRef);
                final int originalSize = astBuffer.getOriginalSize(parseTreeRef);

                iterateTypeReferences(
                        compilationUnit.getReplaceBuffer().getASTReadBuffer(),
                        replacementIndex,
                        compilationUnit,
                        visitor);

                nextParseTreeRef = parseTreeRef + originalSize;
                break;

            case REPLACE_END:
                done = true;
                break;

            default:
                break;
            }

            parseTreeRef = nextParseTreeRef;

        } while (!done);
    }
}
