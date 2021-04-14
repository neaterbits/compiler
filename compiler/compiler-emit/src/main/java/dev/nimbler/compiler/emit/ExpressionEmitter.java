package dev.nimbler.compiler.emit;

import dev.nimbler.compiler.ast.objects.expression.ExpressionVisitor;

public interface ExpressionEmitter<T extends EmitterState> extends ExpressionVisitor<T, Void> {

}
