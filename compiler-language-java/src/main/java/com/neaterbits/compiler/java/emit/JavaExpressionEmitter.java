package com.neaterbits.compiler.java.emit;

import com.neaterbits.compiler.ast.expression.ArrayCreationExpression;
import com.neaterbits.compiler.ast.expression.Base;
import com.neaterbits.compiler.ast.expression.BlockLambdaExpression;
import com.neaterbits.compiler.ast.expression.ClassInstanceCreationExpression;
import com.neaterbits.compiler.ast.expression.FieldAccess;
import com.neaterbits.compiler.ast.expression.FunctionCallExpression;
import com.neaterbits.compiler.ast.expression.FunctionPointerInvocationExpression;
import com.neaterbits.compiler.ast.expression.LambdaExpressionParameters;
import com.neaterbits.compiler.ast.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.expression.SingleLambdaExpression;
import com.neaterbits.compiler.ast.expression.ThisPrimary;
import com.neaterbits.compiler.ast.expression.VariableExpression;
import com.neaterbits.compiler.ast.expression.literal.BooleanLiteral;
import com.neaterbits.compiler.ast.expression.literal.CharacterLiteral;
import com.neaterbits.compiler.ast.expression.literal.ClassExpression;
import com.neaterbits.compiler.ast.expression.literal.FloatingPointLiteral;
import com.neaterbits.compiler.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.ast.expression.literal.NullLiteral;
import com.neaterbits.compiler.ast.expression.literal.StringLiteral;
import com.neaterbits.compiler.ast.operator.Bitwise;
import com.neaterbits.compiler.ast.operator.Operator;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.base.c.CLikeExpressionEmitter;
import com.neaterbits.compiler.util.Strings;

final class JavaExpressionEmitter extends CLikeExpressionEmitter<EmitterState> {


	private static final JavaStatementEmitter STATEMENT_EMITTER = new JavaStatementEmitter();
	
	private static final JavaTypeEmitter TYPE_EMITTER = new JavaTypeEmitter();

	private static final JavaVariableReferenceEmitter VARIABLE_REFERENCE_EMITTER = new JavaVariableReferenceEmitter();

	@Override
	protected void emitStatement(Statement statement, EmitterState state) {
		statement.visit(STATEMENT_EMITTER, state);
	}

	@Override
	protected void emitTypeReference(TypeReference type, EmitterState param) {
		getType(type).visit(TYPE_EMITTER, param);
	}
	
	protected void emitVariableReference(VariableReference reference, EmitterState param) {
		reference.visit(VARIABLE_REFERENCE_EMITTER, param);
	}
	
	@Override
	protected String getOperatorString(Operator operator) {
		
		final String s;
		
		if (operator == Bitwise.RIGHTSHIFT_UNSIGNED) {
			s = ">>>";
		}
		else {
			s = super.getOperatorString(operator);
		}

		return s;
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
	public Void onFunctionPointerInvocation(FunctionPointerInvocationExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onClassInstanceCreation(ClassInstanceCreationExpression expression, EmitterState state) {
		state.append("new ");
		
		emitTypeReference(expression.getTypeReference(), state);
		
		state.append('(');

		emitListTo(state, expression.getParameters().getList(), ", ", param -> emitExpression(param, state));
		
		state.append(')');
		
		return null;
	}

	
	@Override
	public Void onMethodInvocation(MethodInvocationExpression expression, EmitterState param) {
		
		switch (expression.getInvocationType()) {
		case NO_OBJECT:
			break;
			
		case NAMED_CLASS_STATIC:
			emitTypeReference(expression.getClassType(), param);
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
			emitTypeReference(expression.getClassType(), param);
			param.append('.');
			param.append("super").append('.');
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown method invocation type " + expression.getInvocationType());
		}

		param.append(expression.getCallable().getName());
		param.append('(');
		
		emitListTo(param, expression.getParameters().getList(), ", ", e -> emitExpression(e, param));

		param.append(')');
		
		return null;
	}

	@Override
	public Void onArrayCreationExpression(ArrayCreationExpression expression, EmitterState param) {

		param.append("new ");
		
		emitTypeReference(expression.getTypeReference(), param);
		
		param.append(' ');
		
		emitListTo(param, expression.getDimExpressions(), null, e -> {
			param.append('[');
			
			emitExpression(e, param);
			
			param.append(']');
		});
		
		for (int i = 0; i < expression.getNumDims(); ++ i) {
			param.append("[]");
		}

		return null;
	}

	@Override
	public Void onClassExpression(ClassExpression expression, EmitterState param) {
		
		param.append(expression.getName().getName());
		
		param.append(".class");

		for (int i = 0; i < expression.getNumArrayDims(); ++ i) {
			param.append("[]");
		}

		return null;
	}

	@Override
	public Void onFieldAccess(FieldAccess expression, EmitterState param) {

		switch (expression.getFieldAccessType()) {
		case FIELD:
			param.append('.').append(expression.getFieldName().getName());
			break;

		case SUPER_FIELD:
			param.append("super.").append(expression.getFieldName().getName());
			break;
			
		case TYPE_SUPER_FIELD:
			emitTypeReference(expression.getClassType(), param);
			param.append('.').append("super.").append(expression.getFieldName().getName());
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown field access type " + expression.getType());
		}
		
		return null;
	}

	private void emitLambdaParameters(LambdaExpressionParameters parameters, EmitterState param) {
		
		if (parameters.getSingleParameter() != null) {
			param.append(parameters.getSingleParameter());
		}
		else if (parameters.getInferredParameters() != null) {
			param.append('(');

			emitListTo(param, parameters.getInferredParameters(), ", ", parameter -> param.append(parameter));
			
			param.append(')');
		}
		else {
			throw new UnsupportedOperationException("No lambda parameters");
		}
	}
	
	
	@Override
	public Void onSingleLambdaExpression(SingleLambdaExpression expression, EmitterState param) {

		emitLambdaParameters(expression.getParameters(), param);
		
		param.append(" -> ");
		
		emitExpression(expression.getExpression(), param);
		
		return null;
	}

	@Override
	public Void onBlockLambdaExpression(BlockLambdaExpression expression, EmitterState param) {

		emitLambdaParameters(expression.getParameters(), param);
		
		param.append(" -> {").newline();
		
		emitIndentedBlock(expression.getBlock(), param);
		
		param.append('}');
		
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
