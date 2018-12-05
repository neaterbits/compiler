package com.neaterbits.compiler.bytecode.common;

import com.neaterbits.compiler.common.ast.type.complex.InvocableType;

public interface ClassBytecodeTyped {

	InvocableType<?> getType();

}
