package com.neaterbits.compiler.codemap.compiler;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.TypesMap;
import com.neaterbits.compiler.codemap.SynchronizedCodeMap;

public final class SynchronizedCompilerCodeMap
        extends SynchronizedCodeMap
        implements CompilerCodeMap {

    private final CompilerCodeMap delegate;
    
    public SynchronizedCompilerCodeMap(CompilerCodeMap delegate) {
        super(delegate);
        
        this.delegate = delegate;
    }

    @Override
    public synchronized int getTokenForParseTreeRef(int sourceFile, int parseTreeRef) {

        return delegate.getTokenForParseTreeRef(sourceFile, parseTreeRef);
    }

    @Override
    public synchronized int getParseTreeRefForToken(int token) {

        return delegate.getParseTreeRefForToken(token);
    }

    @Override
    public synchronized int getVariableDeclarationTokenReferencedFrom(int fromReferenceToken) {

        return delegate.getVariableDeclarationTokenReferencedFrom(fromReferenceToken);
    }

    @Override
    public synchronized int getMethodDeclarationTokenReferencedFrom(int fromReferenceToken) {

        return delegate.getMethodDeclarationTokenReferencedFrom(fromReferenceToken);
    }

    @Override
    public synchronized int addToken(int sourceFile, int parseTreeRef) {

        return delegate.addToken(sourceFile, parseTreeRef);
    }

    @Override
    public synchronized void addTokenVariableReference(int fromToken, int toDeclarationToken) {

        delegate.addTokenVariableReference(fromToken, toDeclarationToken);
    }

    @Override
    public synchronized int addFile(String file, int [] types) {

        return delegate.addFile(file, types);
    }

    @Override
    public synchronized void addTypeMapping(TypeName name, int typeNo) {

        delegate.addTypeMapping(name, typeNo);
    }

    @Override
    public synchronized Integer getTypeNoByTypeName(TypeName typeName) {

        return delegate.getTypeNoByTypeName(typeName);
    }

    @Override
    public synchronized TypesMap<TypeName> makeTypesMap() {
        
        return delegate.makeTypesMap();
    }

    @Override
    public synchronized void removeFile(String file) {
        
        delegate.removeFile(file);
    }
}
