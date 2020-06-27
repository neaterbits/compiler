package com.neaterbits.compiler.ast.objects.annotation;

import java.util.Collection;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class Annotation extends BaseASTElement {

    private final ScopedName name;
    private final ASTList<AnnotationElement> elements;

    public Annotation(Context context, ScopedName name, Collection<AnnotationElement> elements) {
        super(context);
        
        this.name = name;
        this.elements = makeList(elements);
    }

    public ScopedName getScopedName() {
        return name;
    }

    public ASTList<AnnotationElement> getElements() {
        return elements;
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.ANNOTATION;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
        
        doIterate(elements, recurseMode, iterator);
        
    }
}
