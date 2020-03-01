package com.neaterbits.compiler.emit;

import com.neaterbits.compiler.ast.objects.statement.SwitchCaseLabelVisitor;

public interface SwitchCaseLabelEmitter<T extends EmitterState> extends SwitchCaseLabelVisitor<T, Void> {

}
