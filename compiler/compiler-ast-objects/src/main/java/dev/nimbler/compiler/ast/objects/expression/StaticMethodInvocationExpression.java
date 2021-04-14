package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.block.MethodName;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.method.MethodInvocationType;

public final class StaticMethodInvocationExpression extends ResolvedMethodInvocationExpression {

    private final ASTSingle<TypeReference> classType; // non null for static method calls

    public StaticMethodInvocationExpression(
            Context context,
            TypeReference classType,
            MethodName callable,
            ParameterList parameters) {

        super(context, MethodInvocationType.NAMED_CLASS_STATIC, callable, parameters);
        
        Objects.requireNonNull(classType);
        
        this.classType = makeSingle(classType);
    }

    public TypeReference getClassType() {
        return classType.get();
    }

    @Override
    protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

        doIterate(classType, recurseMode, iterator);
        
        super.doRecurse(recurseMode, iterator);
    }
}
