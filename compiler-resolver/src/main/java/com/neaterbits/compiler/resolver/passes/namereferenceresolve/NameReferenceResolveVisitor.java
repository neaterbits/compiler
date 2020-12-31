package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.UnknownReferenceError;
import com.neaterbits.compiler.resolver.passes.BaseScopesVisitor;
import com.neaterbits.compiler.resolver.passes.ScopeVariableDeclaration;
import com.neaterbits.compiler.resolver.passes.VariableScope;
import com.neaterbits.compiler.util.parse.ScopesListener;

final class NameReferenceResolveVisitor
        extends BaseScopesVisitor<ScopeVariableDeclaration>
        implements ScopesListener {

    private final List<ResolveError> errors;
    
    private final Primaries primaries;

    NameReferenceResolveVisitor(List<ResolveError> errors) {
        
        Objects.requireNonNull(errors);
        
        this.errors = errors;

        this.primaries = new Primaries();
    }

    @Override
    public void onScopeVariableDeclaration(int variableDeclarationParseTreeRef, String name, TypeName type) {

        final VariableScope<ScopeVariableDeclaration> scope = getCurrentScope();
            
        scope.addVariableDeclaration(
                variableDeclarationParseTreeRef,
                name,
                new ScopeVariableDeclaration(type));
    }
    
    @Override
    public <T> void onNonPrimaryListNameReference(int nameParseTreeRef, String name) {

    }

    @Override
    public <T> T onPrimaryListStart(T primaryList, int primaryListParseTreeRef, int size) {

        primaries.push(new PrimaryListEntry(primaryListParseTreeRef));
        
        return null;
    }

    @Override
    public <T> void onPrimaryListEnd(T primaryList, int primaryListParseTreeRef) {

        primaries.pop();
        
    }

    @Override
    public <T> void onPrimaryListNameReference(T primaryList, int nameParseTreeRef, String name) {
        
        final PrimaryListEntry entry = primaries.getEntry();
        
        if (entry.isEmpty()) {
            
            // Initial entry, eg a in a.b.c
            // Find name in scopes
            // final String name = compilationUnitModel.getTokenString(compilationUnit, nameParseTreeRef);
            final ScopeVariableDeclaration declaration = resolveVariableDeclaration(name);
            
            if (declaration != null) {
                entry.addType(declaration.getType());
            }
            else {
                errors.add(new UnknownReferenceError("Unknown name " + name));
            }
        }
        else {
            
            // Field, eg b in a.b.c
            
        }
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
