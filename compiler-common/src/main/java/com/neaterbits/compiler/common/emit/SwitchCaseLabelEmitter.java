package com.neaterbits.compiler.common.emit;

import com.neaterbits.compiler.common.ast.statement.SwitchCaseLabelVisitor;

public interface SwitchCaseLabelEmitter<T extends EmitterState> extends SwitchCaseLabelVisitor<T, Void> {

}
