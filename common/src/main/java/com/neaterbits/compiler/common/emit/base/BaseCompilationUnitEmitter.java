package com.neaterbits.compiler.common.emit.base;


import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationCodeLines;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.ProgramEmitter;

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
