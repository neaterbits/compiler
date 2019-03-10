package com.neaterbits.compiler.emit.base;


import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.CompilationCodeLines;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.ProgramEmitter;

public abstract class BaseCompilationUnitEmitter<T extends EmitterState>
	extends BaseEmitter<T>
	implements ProgramEmitter<T> {

	@Override
	public final Void onStatement(Statement statement, T param) {
		emitStatement(statement, param);
		
		return null;
	}

	protected final void emitCode(CompilationCodeLines code, T param) {
		emitCode(code.getCode(), param);
	}

	protected final void emitCode(Iterable<? extends CompilationCode> code, T param) {
		for (CompilationCode c : code) {
			c.visit(this, param);
		}
	}
}
