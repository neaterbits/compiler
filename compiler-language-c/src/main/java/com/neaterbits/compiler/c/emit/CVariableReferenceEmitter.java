package com.neaterbits.compiler.c.emit;

import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.statement.Statement;
import com.neaterbits.compiler.ast.objects.variables.ArrayAccessReference;
import com.neaterbits.compiler.ast.objects.variables.FieldAccessReference;
import com.neaterbits.compiler.ast.objects.variables.NameReference;
import com.neaterbits.compiler.ast.objects.variables.PrimaryListVariableReference;
import com.neaterbits.compiler.ast.objects.variables.StaticMemberReference;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.VariableReferenceEmitter;
import com.neaterbits.compiler.emit.base.BaseEmitter;

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
