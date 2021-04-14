package com.neaterbits.compiler.resolver.passes.addtokenrefspass;

import com.neaterbits.compiler.resolver.passes.ScopeVariableDeclaration;
import com.neaterbits.language.common.types.TypeName;

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
