package com.neaterbits.compiler.java.emit;

import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.expression.FunctionCallExpression;
import com.neaterbits.compiler.common.ast.expression.VariableExpression;
import com.neaterbits.compiler.common.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.c.CLikeExpressionEmitter;
import com.neaterbits.compiler.common.util.Strings;

final class JavaExpressionEmitter extends CLikeExpressionEmitter<EmitterState> {

	private static final JavaConditionEmitter CONDITION_EMITTER = new JavaConditionEmitter();

	@Override
	protected void emitCondition(Condition condition, EmitterState param) {
		condition.visit(CONDITION_EMITTER, param);
	}

	@Override
	public Void onVariable(VariableExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onFunctionCall(FunctionCallExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onIntegerLiteral(IntegerLiteral expression, EmitterState param) {

		
		return null;
	}

	@Override
	public Void onFloatingPointLiteral(FloatingPointLiteral expression, EmitterState param) {

		return null;
	}

	@Override
	public Void onBooleanLiteral(BooleanLiteral expression, EmitterState param) {
		param.append(expression.getValue() ? "true" : "false");

		return null;
	}

	@Override
	public Void onCharacterLiteral(CharacterLiteral expression, EmitterState param) {

		final char c = expression.getValue();
		
		switch (c) {
		case '\b':
			param.append("'\\b'");
			break;
		case '\t':
			param.append("'\\t'");
			break;
		case '\n':
			param.append("'\\n'");
			break;
		case '\f':
			param.append("'\\f'");
			break;
		case '\r':
			param.append("'\\r'");
			break;
		case '\"':
			param.append("'\\\"'");
			break;
		case '\'':
			param.append("'\\''");
			break;
		case '\\':
			param.append("'\\\\'");
			break;

		default:
			if (c < 127) {
				if (Character.isLetterOrDigit(c)) {
					param.append('\'').append(c).append('\'');
				}
				else {
					param.append("\\u"+ Strings.toHexString((int)c, 4, true));
				}
			}
		}

		return null;
	}

	@Override
	public Void onStringLiteral(StringLiteral expression, EmitterState param) {
		param.append('"').append(expression.getValue()).append('"');

		return null;
	}

	@Override
	public Void onNullLiteral(NullLiteral expression, EmitterState param) {
		param.append("null");
		
		return null;
	}
}
