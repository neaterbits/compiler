package com.neaterbits.compiler.common.emit;

import com.neaterbits.compiler.common.ast.statement.StatementVisitor;

public interface StatementEmitter<T extends EmitterState> extends StatementVisitor<T, Void> {

}
