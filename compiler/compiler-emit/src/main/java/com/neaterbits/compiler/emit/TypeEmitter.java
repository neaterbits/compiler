package com.neaterbits.compiler.emit;

import com.neaterbits.compiler.ast.objects.type.TypeVisitor;

public interface TypeEmitter<T extends EmitterState> extends TypeVisitor<T, Void> {

}
