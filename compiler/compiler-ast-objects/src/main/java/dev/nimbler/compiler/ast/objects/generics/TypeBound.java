package dev.nimbler.compiler.ast.objects.generics;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.TypeBoundType;

public final class TypeBound extends BaseASTElement {

    private final TypeBoundType type;
    private final ASTSingle<TypeReference> typeReference;
    
    public TypeBound(Context context, TypeBoundType type, TypeReference typeReference) {
        super(context);
    
        Objects.requireNonNull(type);
        Objects.requireNonNull(typeReference);
        
        this.type = type;
        this.typeReference = makeSingle(typeReference);
    }

    public TypeBoundType getType() {
        return type;
    }

    public TypeReference getTypeReference() {
        return typeReference.get();
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        
        return ParseTreeElement.TYPE_BOUND;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
        doIterate(typeReference, recurseMode, iterator);
    }
}
