package com.neaterbits.compiler.parser.recursive.cached.types;

import java.util.Objects;

import com.neaterbits.compiler.parser.recursive.cached.names.NamesList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.MutableContext;

public final class TypeArgumentImpl implements TypeArgument {

    private Type type;
    
    // if isGenericTypeName() is false
    private int referenceStartContext;
    private NamesList referenceTypeNames;
    private TypeArgumentsList referenceTypeGenerics;
    private MutableContext referenceEndContext;
    
    private int wildcardStartContext;
    private MutableContext wildcardEndContext;

    TypeArgumentImpl() {

    }
        
    TypeArgumentImpl(int startContext, NamesList referenceTypeNames, TypeArgumentsList referenceTypeGenerics, Context referenceEndContext) {
        
        this.referenceEndContext = new MutableContext();
        
        initReferenceType(startContext, referenceTypeNames, referenceTypeGenerics, referenceEndContext);
    }
        
    void initReferenceType(int startContext, NamesList referenceTypeNames, TypeArgumentsList referenceTypeGenerics, Context referenceEndContext) {
        
        Objects.requireNonNull(referenceTypeNames);
        
        this.type = Type.REFERENCE;

        this.referenceStartContext = startContext;
        this.referenceTypeNames = referenceTypeNames;
        this.referenceTypeGenerics = referenceTypeGenerics;
        
        if (this.referenceEndContext == null) {
            this.referenceEndContext = new MutableContext();
        }

        this.referenceEndContext.init(referenceEndContext);
    }
    
    void initWildcardType(int startContext, Context endContext) {
        
        if (wildcardEndContext == null) {
            this.wildcardEndContext = new MutableContext(endContext);
        }
        else {
            wildcardEndContext.init(endContext);
        }

        this.type = Type.WILDCARD;
    }

    @Override
    public Type getType() {

        return type;
    }

    @Override
    public int getReferenceStartContext() {
        return referenceStartContext;
    }

    @Override
    public NamesList getReferenceTypeNames() {
        return referenceTypeNames;
    }

    @Override
    public Context getReferenceEndContext() {
        return referenceEndContext;
    }

    @Override
    public TypeArgumentsList getReferenceTypeGenerics() {
        return referenceTypeGenerics;
    }

    @Override
    public int getWildcardStartContext() {
        return wildcardStartContext;
    }

    @Override
    public Context getWildcardEndContext() {
        return wildcardEndContext;
    }
}
