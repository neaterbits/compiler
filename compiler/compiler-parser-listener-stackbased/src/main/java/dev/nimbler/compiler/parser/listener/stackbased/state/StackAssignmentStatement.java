package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ExpressionSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

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
