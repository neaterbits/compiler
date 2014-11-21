package com.neaterbits.compiler.c.emit;

import com.neaterbits.compiler.common.ast.expression.ArrayCreationExpression;
import com.neaterbits.compiler.common.ast.expression.Base;
import com.neaterbits.compiler.common.ast.expression.BlockLambdaExpression;
import com.neaterbits.compiler.common.ast.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.common.ast.expression.FieldAccess;
import com.neaterbits.compiler.common.ast.expression.FunctionCallExpression;
import com.neaterbits.compiler.common.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.common.ast.expression.SingleLambdaExpression;
import com.neaterbits.compiler.common.ast.expression.ThisPrimary;
import com.neaterbits.compiler.common.ast.expression.VariableExpression;
import com.neaterbits.compiler.common.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.ClassExpression;
import com.neaterbits.compiler.common.ast.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.c.CLikeExpressionEmitter;

final class CExpressionEmitter extends CLikeExpressionEmitter<EmitterState> {

	private static final CStatementEmitter STATEMENT_EMITTER = new CStatementEmitter();
	
	protected void emitVariableReference(VariableReference variable, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected void emitStatement(Statement statement, EmitterState state) {
		statement.visit(STATEMENT_EMITTER, state);
	}

	@Override
	public Void onVariable(VariableExpression expression, EmitterState param) {

		emitVariableReference(expression.getReference(), param);

		return null;
	}

	@Override
	public Void onFunctionCall(FunctionCallExpression expression, EmitterState state) {
		
		emitListTo(state, expression.getParameters().getList(), ", ", param -> emitExpression(param, state));
		
		return null;
	}

	@Override
	public Void onClassInstanceCreation(ClassInstanceCreationExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onMethodInvocation(MethodInvocationExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onClassExpression(ClassExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Void onArrayCreationExpression(ArrayCreationExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onSingleLambdaExpression(SingleLambdaExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onBlockLambdaExpression(BlockLambdaExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onFieldAccess(FieldAccess expression, EmitterState param) {

		switch (expression.getFieldAccessType()) {
		case FIELD:
			param.append('.').append(expression.getFieldName().getName());
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown field access type " + expression.getType());
		}
		
		return null;
	}

	@Override
	public Void onThis(ThisPrimary expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onIntegerLiteral(IntegerLiteral expression, EmitterState param) {

		switch (expression.getBase()) {
		case BINARY:
			param.append("0x").append(expression.getValue(), Base.HEX);
			break;
		
		case OCTAL:
			param.append('0').append(expression.getValue(), Base.OCTAL);
			break;
			
		case DECIMAL:
			param.append(expression.getValue(), Base.DECIMAL);
			break;
			
		case HEX:
			param.append("0x").append(expression.getValue(), Base.HEX);
			break;
		}

		if (expression.getBits() == 64) {
			param.append(expression.isSigned() ? "l" : "ul");
		}
		
		return null;
	}

	@Override
	public Void onFloatingPointLiteral(FloatingPointLiteral expression, EmitterState param) {

		param.append(expression.getValue().toPlainString());
		
		if (expression.getBits() == 32) {
			param.append('f');
		}
		
		return null;
	}

	@Override
	public Void onBooleanLiteral(BooleanLiteral expression, EmitterState param) {
		
		param.append(expression.getValue() ? "TRUE" : "FALSE");
		
		return null;
	}

	@Override
	public Void onCharacterLiteral(CharacterLiteral expression, EmitterState param) {

		if (expression.getValue() > 255) {
			throw new IllegalArgumentException("Not utf8 char");
		}

		param.append("'\\" + (int)expression.getValue() + "'");

		return null;
	}

	@Override
	public Void onStringLiteral(StringLiteral expression, EmitterState param) {

		param.append('"').append(expression.getValue()).append('"');
		
		return null;
	}

	@Override
	public Void onNullLiteral(NullLiteral expression, EmitterState param) {
		
		param.append("NULL");

		return null;
	}
}
