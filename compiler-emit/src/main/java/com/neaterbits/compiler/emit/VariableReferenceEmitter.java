package com.neaterbits.compiler.emit;

import com.neaterbits.compiler.ast.variables.VariableReferenceVisitor;

public interface VariableReferenceEmitter<T extends EmitterState> extends VariableReferenceVisitor<T, Void> {

}
