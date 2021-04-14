package dev.nimbler.compiler.resolver.passes.addtokenrefspass;

import dev.nimbler.compiler.resolver.passes.ScopeVariableDeclaration;
import dev.nimbler.language.common.types.TypeName;

final class TokenScopeVariableDeclaration extends ScopeVariableDeclaration {

    private final int tokenNo;

    TokenScopeVariableDeclaration(TypeName type, int typeReferenceParseTreeRef, int tokenNo) {
        super(type, typeReferenceParseTreeRef);
    
        this.tokenNo = tokenNo;
    }

    int getTokenNo() {
        return tokenNo;
    }
}
