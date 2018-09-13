package com.neaterbits.compiler.common.emit;

import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;

public interface ExpressionEmitter<T extends EmitterState> extends ExpressionVisitor<T, Void> {

}
