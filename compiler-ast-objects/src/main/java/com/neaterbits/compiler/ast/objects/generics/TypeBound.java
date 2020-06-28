package com.neaterbits.compiler.ast.objects.generics;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.TypeBoundType;

public final class TypeBound extends BaseASTElement {

    private final TypeBoundType type;
    private final ScopedName scopedName;
    
    public TypeBound(Context context, TypeBoundType type, ScopedName scopedName) {
        super(context);
    
        Objects.requireNonNull(type);
        Objects.requireNonNull(scopedName);
        
        this.type = type;
        this.scopedName = scopedName;
    }

    public TypeBoundType getType() {
        return type;
    }
    
    public ScopedName getScopedName() {
        return scopedName;
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        
        return ParseTreeElement.TYPE_BOUND;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
    }
}
