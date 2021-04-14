package com.neaterbits.compiler.emit;

import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;

public interface ExpressionEmitter<T extends EmitterState> extends ExpressionVisitor<T, Void> {

}
