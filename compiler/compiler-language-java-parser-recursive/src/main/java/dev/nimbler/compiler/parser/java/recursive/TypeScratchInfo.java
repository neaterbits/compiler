package dev.nimbler.compiler.parser.java.recursive;

import java.util.Objects;

import com.neaterbits.util.io.strings.StringRef;
import com.neaterbits.util.parse.context.Context;
import com.neaterbits.util.parse.context.MutableContext;

import dev.nimbler.compiler.types.ReferenceType;
import dev.nimbler.compiler.util.ContextRef;
import dev.nimbler.compiler.util.name.Names;

final class TypeScratchInfo {

    private final MutableContext afterTypeContext;

    private long nonScopedName;
    private int nonScopedContext;
    private Names scoped;
    
    private ReferenceType referenceType;
    
    TypeScratchInfo() {
        this.afterTypeContext = new MutableContext();
    }

    void initScopedOrNonScoped(Names scoped, Context afterTypeContext, ReferenceType referenceType) {
        
        switch (scoped.count()) {
        case 0:
            throw new IllegalArgumentException();
            
        case 1:
            initNonScoped(scoped.getStringAt(0), scoped.getContextAt(0), afterTypeContext, referenceType);
            break;
            
        default:
            initScoped(scoped, afterTypeContext, referenceType);
            break;
        }
    }

    void initScoped(Names scoped, Context afterTypeContext, ReferenceType referenceType) {

        Objects.requireNonNull(scoped);
        Objects.requireNonNull(afterTypeContext);
        Objects.requireNonNull(referenceType);
        
        this.scoped = scoped;
        this.afterTypeContext.init(afterTypeContext);
        this.referenceType = referenceType;

        this.nonScopedName = StringRef.STRING_NONE;
    }
    
    void initNonScoped(long name, int context, Context afterTypeContext, ReferenceType referenceType) {
        
        if (name == StringRef.STRING_NONE) {
            throw new IllegalArgumentException();
        }
        
        if (context == ContextRef.NONE) {
            throw new IllegalArgumentException();
        }
        
        Objects.requireNonNull(afterTypeContext);
        Objects.requireNonNull(referenceType);
        
        this.nonScopedName = name;
        this.nonScopedContext = context;
        this.afterTypeContext.init(afterTypeContext);
        this.referenceType = referenceType;

        this.scoped = null;
    }
    
    void initNoType() {
        this.nonScopedName = StringRef.STRING_NONE;
        this.scoped = null;
        this.referenceType = ReferenceType.NONE;
    }

    int getStartContext() {
        return isScoped() ? scoped.getContextAt(0) : nonScopedContext;
    }

    Names getScoped() {
        return scoped;
    }
    
    ReferenceType getReferenceType() {
        return referenceType;
    }
    
    boolean isScoped() {
        return scoped != null;
    }
    
    long getNonScopedName() {
        return nonScopedName;
    }
    
    int getNonScopedContext() {
        return nonScopedContext;
    }

    Context getAfterTypeContext() {
        return afterTypeContext;
    }
}
