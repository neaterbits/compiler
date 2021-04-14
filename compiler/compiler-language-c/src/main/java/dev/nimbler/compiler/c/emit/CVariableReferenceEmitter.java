package dev.nimbler.compiler.c.emit;

import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.variables.ArrayAccessReference;
import dev.nimbler.compiler.ast.objects.variables.FieldAccessReference;
import dev.nimbler.compiler.ast.objects.variables.NameReference;
import dev.nimbler.compiler.ast.objects.variables.PrimaryListVariableReference;
import dev.nimbler.compiler.ast.objects.variables.StaticMemberReference;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.VariableReferenceEmitter;
import dev.nimbler.compiler.emit.base.BaseEmitter;

public class CVariableReferenceEmitter
		extends BaseEmitter<EmitterState>
		implements VariableReferenceEmitter<EmitterState> {

	private static final CExpressionEmitter EXPRESSION_EMITTER = new CExpressionEmitter();
	
	
	protected final void emitExpression(Expression expression, EmitterState param) {
		expression.visit(EXPRESSION_EMITTER, param);
	}
	
	@Override
	public Void onNameReference(NameReference nameReference, EmitterState param) {
		
		param.append(nameReference.getName());

		return null;
	}

	@Override
	public Void onArrayAccessReference(ArrayAccessReference variableReference, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onFieldAccessReference(FieldAccessReference fieldAccessReference, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onStaticMemberReference(StaticMemberReference staticMemberReference, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onPrimaryList(PrimaryListVariableReference primaryListVariableReference, EmitterState param) {
		
		emitListTo(
				param,
				primaryListVariableReference.getList().getPrimaries(),
				".",
				primary -> emitExpression(primary, param));
		
		return null;
	}

	@Override
	protected void emitStatement(Statement statement, EmitterState state) {
		throw new UnsupportedOperationException();
	}
}
