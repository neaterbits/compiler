package dev.nimbler.compiler.emit;

import dev.nimbler.compiler.ast.objects.statement.SwitchCaseLabelVisitor;

public interface SwitchCaseLabelEmitter<T extends EmitterState> extends SwitchCaseLabelVisitor<T, Void> {

}
