package com.neaterbits.compiler.model.encoded;

import java.io.PrintStream;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.model.common.FieldVisitor;
import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.compiler.model.common.MethodVisitor;
import com.neaterbits.compiler.model.common.ProgramModel;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.model.common.SourceTokenUtil;
import com.neaterbits.compiler.model.common.SourceTokenUtil.ASTAccess;
import com.neaterbits.compiler.model.common.SourceTokenVisitor;
import com.neaterbits.compiler.model.common.TypeVisitor;
import com.neaterbits.compiler.model.common.UserDefinedTypeRef;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.imports.TypeImport;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.ScopesListener;

public final class EncodedProgramModel
    extends EncodedImportsModel
    implements ProgramModel<EncodedModule, EncodedParsedFile, EncodedCompilationUnit> {

    private static final ASTAccess<Integer, EncodedCompilationUnit> AST_ACCESS = new EncodedASTAccess();

    public EncodedProgramModel(List<TypeImport> implicitImports) {
        super(implicitImports);
    }


    @Override
    public void iterate(
            EncodedCompilationUnit sourceFile,
            SourceTokenVisitor iterator,
            ResolvedTypes resolvedTypes,
            boolean visitPlaceholderElements) {

    }

    @Override
    public ISourceToken getTokenAtOffset(
            EncodedCompilationUnit sourceFile,
            long offset,
            ResolvedTypes resolvedTypes) {

        // FIXME Auto-generated method stub
        return null;
    }

    @Override
    public ISourceToken getTokenAtParseTreeRef(
            EncodedCompilationUnit sourceFile,
            int parseTreeRef,
            ResolvedTypes resolvedTypes) {

        return SourceTokenUtil.makeSourceToken(
                parseTreeRef,
                sourceFile,
                resolvedTypes,
                this,
                AST_ACCESS);
    }

    @Override
    public int getTokenOffset(EncodedCompilationUnit sourceFile, int parseTreeTokenRef) {

        return sourceFile.getTokenOffset(parseTreeTokenRef);
    }

    @Override
    public int getTokenLength(EncodedCompilationUnit sourceFile, int parseTreeTokenRef) {

        return sourceFile.getTokenLength(parseTreeTokenRef);
    }

    @Override
    public String getTokenString(EncodedCompilationUnit sourceFile, int parseTreeTokenRef) {

        return sourceFile.getTokenString(parseTreeTokenRef);
    }

    @Override
    public void iterateScopesAndVariables(EncodedCompilationUnit sourceFile, ScopesListener scopesListener) {
        // FIXME Auto-generated method stub

    }

    @Override
    public String getMethodName(EncodedCompilationUnit sourceFile, int parseTreeMethodDeclarationRef) {
        // FIXME Auto-generated method stub
        return null;
    }

    @Override
    public String getVariableName(
            EncodedCompilationUnit sourceFile,
            int parseTreeVariableDeclarationRef) {

        return sourceFile.getStringFromASTBufferOffset(parseTreeVariableDeclarationRef + 1);
    }

    @Override
    public String getClassDataFieldMemberName(
            EncodedCompilationUnit sourceFile,
            int parseTreeDataMemberDeclarationRef) {

        return sourceFile.getStringFromASTBufferOffset(parseTreeDataMemberDeclarationRef + 1);
    }

    @Override
    public String getClassName(
            EncodedCompilationUnit sourceFile,
            int parseTreeTypeDeclarationRef) {
        // FIXME Auto-generated method stub
        return null;
    }

    @Override
    public void print(EncodedCompilationUnit sourceFile, PrintStream out) {
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
        // TODO Auto-generated method stub
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
    public EncodedParsedFile getParsedFile(EncodedModule module, FileSpec path) {

        return module.getParsedFiles().stream()
                .filter(pf -> pf.getFileSpec().equals(path))
                .findFirst()
                .orElse(null);
    }

    @Override
    public EncodedCompilationUnit getCompilationUnit(EncodedParsedFile sourceFile) {

        return sourceFile.getCompilationUnit();
    }
}
