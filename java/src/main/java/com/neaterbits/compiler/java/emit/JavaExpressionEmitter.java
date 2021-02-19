package com.neaterbits.compiler.java.emit;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.condition.Condition;
import com.neaterbits.compiler.common.ast.expression.Base;
import com.neaterbits.compiler.common.ast.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.common.ast.expression.FieldAccess;
import com.neaterbits.compiler.common.ast.expression.FunctionCallExpression;
import com.neaterbits.compiler.common.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.common.ast.expression.ThisPrimary;
import com.neaterbits.compiler.common.ast.expression.VariableExpression;
import com.neaterbits.compiler.common.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.common.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.base.c.CLikeExpressionEmitter;
import com.neaterbits.compiler.common.util.Strings;

final class JavaExpressionEmitter extends CLikeExpressionEmitter<EmitterState> {

	private static final JavaConditionEmitter CONDITION_EMITTER = new JavaConditionEmitter();
	
	private static final JavaTypeEmitter TYPE_EMITTER = new JavaTypeEmitter();

	private static final JavaVariableReferenceEmitter VARIABLE_REFERENCE_EMITTER = new JavaVariableReferenceEmitter();

	@Override
	protected void emitCondition(Condition condition, EmitterState param) {
		condition.visit(CONDITION_EMITTER, param);
	}

	private void emitType(TypeReference type, EmitterState param) {
		type.getType().visit(TYPE_EMITTER, param);
	}
	
	protected void emitVariableReference(VariableReference reference, EmitterState param) {
		reference.visit(VARIABLE_REFERENCE_EMITTER, param);
	}
	
	@Override
	public Void onVariable(VariableExpression expression, EmitterState param) {
		
		emitVariableReference(expression.getReference(), param);

		return null;
	}

	@Override
	public Void onFunctionCall(FunctionCallExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}
	

	@Override
	public Void onClassInstanceCreation(ClassInstanceCreationExpression expression, EmitterState state) {
		state.append("new ");
		
		emitType(expression.getType(), state);
		
		state.append('(');

		emitListTo(state, expression.getParameters().getList(), ", ", param -> emitExpression(param, state));
		
		state.append(')');
		
		return null;
	}

	
	@Override
	public Void onMethodInvocation(MethodInvocationExpression expression, EmitterState param) {
		
		switch (expression.getType()) {
		case NO_OBJECT:
			break;
			
		case NAMED_CLASS_STATIC:
			emitType(expression.getClassType(), param);
			param.append('.');
			break;

		case VARIABLE_REFERENCE:
		case PRIMARY:
			emitExpression(expression.getObject(), param);
			param.append('.');
			break;
			
		case SUPER:
			param.append("super").append('.');
			break;
			
		case TYPED_SUPER:
			emitType(expression.getClassType(), param);
			param.append('.');
			param.append("super").append('.');
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown method invocation type " + expression.getType());
		}

		param.append(expression.getCallable().getName());
		param.append('(');
		
		emitListTo(param, expression.getParameters().getList(), ", ", e -> emitExpression(e, param));

		param.append(')');
		
		return null;
	}

	@Override
	public Void onFieldAccess(FieldAccess expression, EmitterState param) {

		switch (expression.getType()) {
		case FIELD:
			param.append('.').append(expression.getFieldName().getName());
			break;

		case SUPER_FIELD:
			param.append("super.").append(expression.getFieldName().getName());
			break;
			
		case TYPE_SUPER_FIELD:
			emitType(expression.getClassType(), param);
			param.append('.').append("super.").append(expression.getFieldName().getName());
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown field access type " + expression.getType());
		}
		
		return null;
	}

	@Override
	public Void onThis(ThisPrimary expression, EmitterState param) {

		param.append("this");

		return null;
	}

	@Override
	public Void onIntegerLiteral(IntegerLiteral expression, EmitterState param) {

		switch (expression.getBase()) {
		case BINARY:
			param.append("0b").append(expression.getValue(), Base.BINARY);
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
			param.append('l');
		}
		
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
