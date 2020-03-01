package com.neaterbits.compiler.emit;

import com.neaterbits.compiler.ast.objects.statement.StatementVisitor;

public interface StatementEmitter<T extends EmitterState> extends StatementVisitor<T, Void> {

}
