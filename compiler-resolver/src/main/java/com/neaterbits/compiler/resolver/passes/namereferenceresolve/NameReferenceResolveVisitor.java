package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.util.List;
import java.util.Objects;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.codemap.compiler.CrossReferenceUpdater;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.UnknownReferenceError;
import com.neaterbits.compiler.util.parse.ScopesListener;

final class NameReferenceResolveVisitor implements ScopesListener {

    private final CrossReferenceUpdater crossReference;
    private final int sourceFile;
    private final boolean addTokenRefs;
    private final List<ResolveError> errors;
    
    private final Scopes scopes;
    private final Primaries primaries;

    NameReferenceResolveVisitor(
            CrossReferenceUpdater crossReference,
            int sourceFile,
            boolean addTokenRefs,
            List<ResolveError> errors) {
        
        Objects.requireNonNull(crossReference);
        Objects.requireNonNull(errors);
        
        this.crossReference = crossReference;
        this.sourceFile = sourceFile;
        this.addTokenRefs = addTokenRefs;
        this.errors = errors;

        this.scopes = new Scopes();
        this.primaries = new Primaries();
    }

    @Override
    public void onClassStart(int classParseTreeRef) {

        scopes.push();
    }

    @Override
    public void onMethodScopeStart(int methodParseTreeRef) {

        if (addTokenRefs) {
            crossReference.addToken(sourceFile, methodParseTreeRef);
        }

        scopes.push();
    }

    @Override
    public void onBlockScopeStart(int blockRef) {

        scopes.push();
        
    }

    @Override
    public void onScopeVariableDeclaration(int variableDeclarationParseTreeRef, String name, TypeName type) {

        final VariableScope scope = scopes.getCurrentScope();

        if (addTokenRefs) {
            final int declarationCrossReferenceToken = crossReference.addToken(sourceFile, variableDeclarationParseTreeRef);
            
            scope.addVariableDeclaration(variableDeclarationParseTreeRef, declarationCrossReferenceToken, name, type);
        }
    }
    
    @Override
    public <T> void onNonPrimaryListNameReference(int nameParseTreeRef, String name) {

        final VariableDeclaration declaration = scopes.resolveVariableDeclaration(name);
        
        if (addTokenRefs && declaration != null) {
            
            final int fromToken = crossReference.addToken(sourceFile, nameParseTreeRef);
            
            crossReference.addTokenVariableReference(fromToken, declaration.getTokenNo());
        }
        else {
            errors.add(new UnknownReferenceError("Unknown name " + name));
        }
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
            final VariableDeclaration declaration = scopes.resolveVariableDeclaration(name);
            
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

    @Override
    public void onBlockScopeEnd(int blockParseTreeRef) {

        scopes.pop();
        
    }

    @Override
    public void onMethodScopeEnd(int methodParseTreeRef) {

        scopes.pop();
        
    }

    @Override
    public void onClassEnd(int classParseTreeRef) {

        scopes.pop();
        
    }
}
