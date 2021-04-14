package dev.nimbler.compiler.ast.objects.annotation;

import java.util.Collection;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.language.common.types.ScopedName;

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
