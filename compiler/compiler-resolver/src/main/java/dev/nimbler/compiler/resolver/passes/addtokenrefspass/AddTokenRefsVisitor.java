package dev.nimbler.compiler.resolver.passes.addtokenrefspass;

import java.util.Objects;

import dev.nimbler.compiler.model.common.ResolvedScopesListener;
import dev.nimbler.compiler.resolver.passes.BaseVariableDeclaratorVisitor;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.common.types.TypeName;

final class AddTokenRefsVisitor<COMPILATION_UNIT>
    extends BaseVariableDeclaratorVisitor<TokenScopeVariableDeclaration>
    implements ResolvedScopesListener {

    private final CompilerCodeMap codeMap;
    private final int sourceFile;

    AddTokenRefsVisitor(CompilerCodeMap codeMap, int sourceFile) {
        
        super(codeMap);

        Objects.requireNonNull(codeMap);
        
        this.codeMap = codeMap;
        this.sourceFile = sourceFile;
    }

    @Override
    public void onMethodScopeStart(int methodParseTreeRef) {

        codeMap.addToken(sourceFile, methodParseTreeRef);

        super.onMethodScopeStart(methodParseTreeRef);
    }

    @Override
    protected TokenScopeVariableDeclaration createVariableDeclaration(
            int variableDeclarationParseTreeRef,
            TypeName type,
            int typeReferenceParseTreeRef) {
        
        final int declarationCrossReferenceToken = codeMap.addToken(sourceFile, variableDeclarationParseTreeRef);
               
        return new TokenScopeVariableDeclaration(type, typeReferenceParseTreeRef, declarationCrossReferenceToken);
    }

    @Override
    public <T> void onNonPrimaryListNameReference(int nameParseTreeRef, String name) {

        final TokenScopeVariableDeclaration declaration = resolveVariableDeclaration(name);
        
        if (declaration != null) {
            
            final int fromToken = codeMap.addToken(sourceFile, nameParseTreeRef);
            
            codeMap.addTokenVariableReference(fromToken, declaration.getTokenNo());
        }
    }

    @Override
    public <T> T onPrimaryListStart(T primaryList, int primaryListParseTreeRef, int size) {

        return null;
    }

    @Override
    public <T> void onPrimaryListEnd(T primaryList, int primaryListParseTreeRef) {

    }

    @Override
    public <T> void onPrimaryListNameReference(T primaryList, int nameParseTreeRef, String name) {
        
    }

    @Override
    public <T> T onArrayAccessReference(T primaryList, int arrayAccessParseTreeRef) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T onFieldAccessReference(T primaryList, int fieldAccessParseTreeRef) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T onStaticMemberReference(T primaryList, int staticMemberParseTreeRef) {
        // TODO Auto-generated method stub
        return null;
    }
}
