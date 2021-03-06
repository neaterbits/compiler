package dev.nimbler.compiler.language.java.emit;

import org.jutils.Strings;

import dev.nimbler.compiler.ast.objects.expression.ArrayCreationExpression;
import dev.nimbler.compiler.ast.objects.expression.BlockLambdaExpression;
import dev.nimbler.compiler.ast.objects.expression.ClassInstanceCreationExpression;
import dev.nimbler.compiler.ast.objects.expression.FieldAccess;
import dev.nimbler.compiler.ast.objects.expression.FunctionCallExpression;
import dev.nimbler.compiler.ast.objects.expression.FunctionPointerInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.LambdaExpressionParameters;
import dev.nimbler.compiler.ast.objects.expression.PrimaryMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.ResolvedMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.SingleLambdaExpression;
import dev.nimbler.compiler.ast.objects.expression.StaticMethodInvocationExpression;
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
import dev.nimbler.compiler.types.operator.Bitwise;
import dev.nimbler.compiler.types.operator.Operator;
import dev.nimbler.compiler.util.Base;

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
    public Void onUnresolvedMethodInvocation(UnresolvedMethodInvocationExpression expression, EmitterState param) {
        
	    param.append('.').append(expression.getCallable().getName());

        return null;
    }

    @Override
	public Void onMethodInvocation(ResolvedMethodInvocationExpression expression, EmitterState param) {
		
		switch (expression.getInvocationType()) {
		case NO_OBJECT:
			break;
			
		case NAMED_CLASS_STATIC:
		    final StaticMethodInvocationExpression staticMethodInvocationExpression
		        = (StaticMethodInvocationExpression)expression;
			emitTypeReference(staticMethodInvocationExpression.getClassType(), param);
			param.append('.');
			break;

		case VARIABLE_REFERENCE:
		case PRIMARY:
		    final PrimaryMethodInvocationExpression primaryMethodInvocationExpression
		        = (PrimaryMethodInvocationExpression)expression;
			emitExpression(primaryMethodInvocationExpression.getObject(), param);
			param.append('.');
			break;
			
		case SUPER:
			param.append("super").append('.');
			break;
			
		case TYPED_SUPER:
		    /*
			emitTypeReference(expression.getClassType(), param);
			param.append('.');
			param.append("super").append('.');
			break;
			*/
		    throw new UnsupportedOperationException();
			
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
	public Void onClassExpression(UnresolvedClassExpression expression, EmitterState param) {
		
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
