package com.neaterbits.compiler.bytecode.ast;

import com.neaterbits.compiler.ast.objects.type.complex.InvocableType;

public interface ClassBytecodeTyped {

	InvocableType<?, ?, ?> getType();

}
