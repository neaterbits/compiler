package dev.nimbler.compiler.emit;

import dev.nimbler.compiler.ast.objects.type.TypeVisitor;

public interface TypeEmitter<T extends EmitterState> extends TypeVisitor<T, Void> {

}
