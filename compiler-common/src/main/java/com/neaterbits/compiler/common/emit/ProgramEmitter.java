package com.neaterbits.compiler.common.emit;

import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;

public interface ProgramEmitter<T extends EmitterState> extends CompilationCodeVisitor<T, Void> {

	
}
