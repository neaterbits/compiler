package dev.nimbler.compiler.emit;

import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;

public interface ProgramEmitter<T extends EmitterState> extends CompilationCodeVisitor<T, Void> {

	
}
