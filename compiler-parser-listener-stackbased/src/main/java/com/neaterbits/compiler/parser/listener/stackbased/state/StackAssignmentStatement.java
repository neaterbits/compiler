package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackAssignmentStatement<EXPRESSION, ASSIGNMENT_EXPRESSION extends EXPRESSION>
    extends StackEntry
    implements ExpressionSetter<EXPRESSION> {
    
    public StackAssignmentStatement(ParseLogger parseLogger) {
        super(parseLogger);
    }

    private EXPRESSION expression;

    @Override
    public void addExpression(EXPRESSION expression) {
        
        Objects.requireNonNull(expression);

        if (this.expression != null) {
            throw new IllegalStateException();
        }
        
        this.expression = expression;
    }

    @SuppressWarnings("unchecked")
    public ASSIGNMENT_EXPRESSION getExpression() {
        return (ASSIGNMENT_EXPRESSION)expression;
    }
}
