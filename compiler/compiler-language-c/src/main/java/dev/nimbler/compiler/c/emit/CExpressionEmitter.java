package dev.nimbler.compiler.c.emit;

import dev.nimbler.compiler.ast.objects.expression.ArrayCreationExpression;
import dev.nimbler.compiler.ast.objects.expression.BlockLambdaExpression;
import dev.nimbler.compiler.ast.objects.expression.ClassInstanceCreationExpression;
import dev.nimbler.compiler.ast.objects.expression.FieldAccess;
import dev.nimbler.compiler.ast.objects.expression.FunctionCallExpression;
import dev.nimbler.compiler.ast.objects.expression.FunctionPointerInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.ResolvedMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.SingleLambdaExpression;
import dev.nimbler.compiler.ast.objects.expression.ThisPrimary;
import dev.nimbler.compiler.ast.objects.expression.UnresolvedMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.literal.BooleanLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.CharacterLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.FloatingPointLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.IntegerLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.NullLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.StringLiteral;
import dev.nimbler.compiler.ast.objects.expression.literal.UnresolvedClassExpression;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.VariableReference;
import dev.nimbler.compiler.emit.EmitterState;
import dev.nimbler.compiler.emit.base.c.CLikeExpressionEmitter;
import dev.nimbler.compiler.util.Base;

final class CExpressionEmitter extends CLikeExpressionEmitter<EmitterState> {

	private static final CStatementEmitter STATEMENT_EMITTER = new CStatementEmitter();
	
	private static final CVariableReferenceEmitter VARIABLE_REFERENCE_EMITTER = new CVariableReferenceEmitter();
	
	private static final CTypeEmitter TYPE_EMITTER = new CTypeEmitter();
	
	protected void emitVariableReference(VariableReference variable, EmitterState param) {
		variable.visit(VARIABLE_REFERENCE_EMITTER, param);
	}

	protected void emitTypeReference(TypeReference type, EmitterState param) {
		getType(type).visit(TYPE_EMITTER, param);
	}

	@Override
	protected void emitStatement(Statement statement, EmitterState state) {
		statement.visit(STATEMENT_EMITTER, state);
	}

	@Override
	public Void onFunctionCall(FunctionCallExpression expression, EmitterState state) {
		
		emitListTo(state, expression.getParameters().getList(), ", ", param -> emitExpression(param, state));
		
		return null;
	}

	@Override
	public Void onFunctionPointerInvocation(FunctionPointerInvocationExpression expression, EmitterState state) {

		emitExpression(expression.getFunctionPointer(), state);

		state.append('(');
		
		emitListTo(state, expression.getParameters().getList(), ", ", param -> emitExpression(param, state));

		state.append(')');
		
		return null;
	}

	@Override
	public Void onClassInstanceCreation(ClassInstanceCreationExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
    public Void onUnresolvedMethodInvocation(UnresolvedMethodInvocationExpression expression, EmitterState param) {
        throw new UnsupportedOperationException();
    }

    @Override
	public Void onMethodInvocation(ResolvedMethodInvocationExpression expression, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void onClassExpression(UnresolvedClassExpression expression, EmitterState param) {
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
