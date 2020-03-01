package com.neaterbits.compiler.emit;

import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;

public interface ProgramEmitter<T extends EmitterState> extends CompilationCodeVisitor<T, Void> {

	
}
