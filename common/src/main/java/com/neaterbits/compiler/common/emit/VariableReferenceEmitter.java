package com.neaterbits.compiler.common.emit;

import com.neaterbits.compiler.common.ast.variables.VariableReferenceVisitor;

public interface VariableReferenceEmitter<T extends EmitterState> extends VariableReferenceVisitor<T, Void> {

}
