package com.neaterbits.compiler.parser.recursive;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextRef;
import com.neaterbits.compiler.util.MutableContext;
import com.neaterbits.util.io.strings.StringRef;

final class TypeArgumentImpl implements TypeArgument {

    private long genericTypeName;
    private int genericTypeNameContext;
    
    // if isGenericTypeName() is false
    private NamesList concreteTypeNames;
    
    private TypeArgumentsList concreteTypeGenerics;
    
    private MutableContext concreteEndContext;

    TypeArgumentImpl(int genericTypeNameContext, long genericTypeName) {
        
        init(genericTypeNameContext, genericTypeName);
    }
        
    void init(int genericTypeNameContext, long genericTypeName) {

        if (genericTypeNameContext == ContextRef.NONE) {
            throw new IllegalArgumentException();
        }

        if (genericTypeName == StringRef.STRING_NONE) {
            throw new IllegalArgumentException();
        }
        
        this.genericTypeNameContext = genericTypeNameContext;
        this.genericTypeName = genericTypeName;
        
        this.concreteTypeNames = null;
        this.concreteTypeGenerics = null;
    }

    TypeArgumentImpl(NamesList concreteTypeNames, TypeArgumentsList concreteTypeGenerics, Context concreteEndContext) {
        
        this.concreteEndContext = new MutableContext();
        
        init(concreteTypeNames, concreteTypeGenerics, concreteEndContext);
    }
        
    void init(NamesList concreteTypeNames, TypeArgumentsList concreteTypeGenerics, Context concreteEndContext) {
        
        Objects.requireNonNull(concreteTypeNames);

        this.genericTypeName = StringRef.STRING_NONE;
        this.genericTypeNameContext = ContextRef.NONE;
        
        this.concreteTypeNames = concreteTypeNames;
        this.concreteTypeGenerics = concreteTypeGenerics;
        this.concreteEndContext.init(concreteEndContext);
    }

    @Override
    public boolean isGenericTypeName() {
        return genericTypeName != StringRef.STRING_NONE;
    }

    @Override
    public long getGenericTypeName() {
        return genericTypeName;
    }

    @Override
    public int getGenericTypeNameContext() {
        return genericTypeNameContext;
    }

    @Override
    public NamesList getConcreteTypeNames() {
        return concreteTypeNames;
    }

    @Override
    public Context getConcreteEndContext() {
        // FIXME Auto-generated method stub
        return null;
    }

    @Override
    public TypeArgumentsList getConcreteTypeGenerics() {
        return concreteTypeGenerics;
    }
}
