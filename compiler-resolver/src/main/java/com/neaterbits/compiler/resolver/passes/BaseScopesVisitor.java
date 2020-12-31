package com.neaterbits.compiler.resolver.passes;

import com.neaterbits.compiler.model.common.ScopesListener;

public abstract class BaseScopesVisitor<T extends ScopeVariableDeclaration>
        extends BaseNamespaceVisitor
        implements ScopesListener {

    private final Scopes<T> scopes;

    protected BaseScopesVisitor() {

        this.scopes = new Scopes<>();
    }
    
    protected final VariableScope<T> getCurrentScope() {
        return scopes.getCurrentScope();
    }
    
    protected final T resolveVariableDeclaration(String name) {
        
        return scopes.resolveVariableDeclaration(name);
    }

    @Override
    public final void onClassStart(int classParseTreeRef) {

        scopes.push();
    }

    @Override
    public void onMethodScopeStart(int methodParseTreeRef) {

        scopes.push();
    }

    @Override
    public final void onBlockScopeStart(int blockRef) {

        scopes.push();
    }

    @Override
    public final void onBlockScopeEnd(int blockParseTreeRef) {

        scopes.pop();
    }

    @Override
    public final void onMethodScopeEnd(int methodParseTreeRef) {

        scopes.pop();
    }

    @Override
    public final void onClassEnd(int classParseTreeRef) {

        scopes.pop();
    }
}
