package com.neaterbits.compiler.model.encoded;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.ast.encoded.AST;
import com.neaterbits.compiler.ast.encoded.ASTBufferRead;
import com.neaterbits.compiler.ast.encoded.ASTBufferRead.ParseTreeElementRef;
import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.model.common.FieldVisitor;
import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.compiler.model.common.MethodVisitor;
import com.neaterbits.compiler.model.common.ProgramModel;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.model.common.SourceTokenUtil;
import com.neaterbits.compiler.model.common.SourceTokenUtil.ASTAccess;
import com.neaterbits.compiler.model.common.SourceTokenVisitor;
import com.neaterbits.compiler.model.common.TypeReferenceVisitor;
import com.neaterbits.compiler.model.common.TypeVisitor;
import com.neaterbits.compiler.model.common.UserDefinedTypeRef;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.imports.TypeImport;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.ScopesListener;

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
    public void iterateScopesAndVariables(EncodedCompilationUnit compilationUnit, ScopesListener scopesListener) {
        // FIXME Auto-generated method stub

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
        // FIXME Auto-generated method stub
        return null;
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
    public void iterateTypes(EncodedCompilationUnit compilationUnit, TypeVisitor visitor) {

        // Iterate through the parse tree
        final ASTBufferRead astBuffer = compilationUnit.getBuffer();

        final ParseTreeElementRef ref = new ParseTreeElementRef();

        int parseTreeRef = 0;

        boolean done = false;

        do {
            astBuffer.getParseTreeElement(parseTreeRef, ref);

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
                    visitor.onClassEnd();
                }
                break;

            case INTERFACE_DEFINITION:
                if (ref.isStart) {
                    final int interfaceNameStringRef = AST.decodeInterfaceName(astBuffer, ref.index);

                    visitor.onInterfaceStart(compilationUnit.getStringFromRef(interfaceNameStringRef));
                }
                else {
                    visitor.onInterfaceEnd();
                }
                break;

            case ENUM_DEFINITION:
                if (ref.isStart) {
                    final int enumNameStringRef = AST.decodeEnumName(astBuffer, ref.index);

                    visitor.onEnumStart(compilationUnit.getStringFromRef(enumNameStringRef));
                }
                else {
                    visitor.onEnumEnd();
                }
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
    public void iterateClassMembers(
            EncodedCompilationUnit compilationUnit,
            UserDefinedTypeRef complexType,
            FieldVisitor fieldVisitor,
            MethodVisitor methodVisitor) {

        // Iterate all elements since they are in sequence. Must use a stack to keep track of level

        // FIXME Auto-generated method stub

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

            case COMPLEX_TYPE_REFERENCE:
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
                        compilationUnit.getReplaceBuffer(),
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
