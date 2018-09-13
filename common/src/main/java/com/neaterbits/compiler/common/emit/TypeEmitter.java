package com.neaterbits.compiler.common.emit;

import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public interface TypeEmitter<T extends EmitterState> extends TypeVisitor<T, Void> {

}
