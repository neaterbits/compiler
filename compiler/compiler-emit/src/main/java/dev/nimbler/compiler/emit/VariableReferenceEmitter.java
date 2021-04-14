package dev.nimbler.compiler.emit;

import dev.nimbler.compiler.ast.objects.variables.VariableReferenceVisitor;

public interface VariableReferenceEmitter<T extends EmitterState> extends VariableReferenceVisitor<T, Void> {

}
