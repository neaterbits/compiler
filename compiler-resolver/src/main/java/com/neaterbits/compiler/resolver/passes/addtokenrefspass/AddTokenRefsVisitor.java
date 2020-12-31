package com.neaterbits.compiler.resolver.passes.addtokenrefspass;

import java.util.Objects;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.resolver.passes.BaseScopesVisitor;
import com.neaterbits.compiler.resolver.passes.VariableScope;

final class AddTokenRefsVisitor<COMPILATION_UNIT> extends BaseScopesVisitor<TokenScopeVariableDeclaration> {

    private final CompilerCodeMap codeMap;
    private final int sourceFile;

    AddTokenRefsVisitor(
            CompilerCodeMap codeMap,
            int sourceFile) {

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
    public void onScopeVariableDeclaration(int variableDeclarationParseTreeRef, String name, TypeName type) {

        final VariableScope<TokenScopeVariableDeclaration> scope = getCurrentScope();

        final int declarationCrossReferenceToken = codeMap.addToken(sourceFile, variableDeclarationParseTreeRef);
            
        scope.addVariableDeclaration(
                variableDeclarationParseTreeRef,
                name,
                new TokenScopeVariableDeclaration(type, declarationCrossReferenceToken));
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
    public <T> void onSimpleVariableReference(T primaryList, int variableParseTreeRef) {
        // TODO Auto-generated method stub
        
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
