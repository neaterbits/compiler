package com.neaterbits.compiler.common.emit;

import com.neaterbits.compiler.common.ast.condition.ConditionVisitor;

public interface ConditionEmitter<T extends EmitterState> extends ConditionVisitor<T, Void> {

}
