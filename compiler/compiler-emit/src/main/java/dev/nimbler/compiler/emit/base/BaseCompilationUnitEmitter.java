package dev.nimbler.compiler.emit.base;


import dev.nimbler.compiler.ast.objects.CompilationCode;
import dev.nimbler.compiler.ast.objects.CompilationCodeLines;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.ProgramEmitter;

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
