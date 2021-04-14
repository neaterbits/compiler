package dev.nimbler.compiler.bytecode.ast;

import dev.nimbler.compiler.ast.objects.type.complex.InvocableType;

public interface ClassBytecodeTyped {

	InvocableType<?, ?, ?> getType();

}
