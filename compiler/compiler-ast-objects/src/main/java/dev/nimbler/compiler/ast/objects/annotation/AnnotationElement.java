package dev.nimbler.compiler.ast.objects.annotation;

import java.util.Collection;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.Name;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class AnnotationElement extends BaseASTElement {

    private final ASTSingle<Name> name;
    private final ASTSingle<Expression> expression;
    private final ASTSingle<Annotation> annotation;
    private final ASTList<AnnotationElement> valueList;
    
    public AnnotationElement(Context context, Name name, Expression expression) {
        super(context);
        
        this.name = name != null ? makeSingle(name) : null;
        this.expression = makeSingle(expression);
        this.annotation = null;
        this.valueList = null;
    }

    public AnnotationElement(Context context, Name name, Annotation annotation) {
        super(context);
        
        this.name = name != null ? makeSingle(name) : null;
        this.expression = null;
        this.annotation = makeSingle(annotation);
        this.valueList = null;
    }

    public AnnotationElement(Context context, Name name, Collection<AnnotationElement> valueList) {
        
        super(context);
        
        this.name = name != null ? makeSingle(name) : null;
        this.expression = null;
        this.annotation = null;
        this.valueList = makeList(valueList);
    }

    public String getName() {
        return name != null ? name.get().getText() : null;
    }

    public Expression getExpression() {
        return expression != null ? expression.get() : null;
    }

    public Annotation getAnnotation() {
        return annotation != null ? annotation.get() : null;
    }

    public ASTList<AnnotationElement> getValueList() {
        return valueList;
    }

    @Override
    public ParseTreeElement getParseTreeElement() {
        return ParseTreeElement.ANNOTATION_ELEMENT;
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        if (expression != null) {
            doIterate(expression, recurseMode, iterator);
        }
        
        if (annotation != null) {
            doIterate(annotation, recurseMode, iterator);
        }

        if (valueList != null) {
            doIterate(valueList, recurseMode, iterator);
        }
    }
}
