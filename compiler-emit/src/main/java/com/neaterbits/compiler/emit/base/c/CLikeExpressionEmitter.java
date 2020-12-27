package com.neaterbits.compiler.emit.base.c;

import com.neaterbits.compiler.ast.objects.expression.ArrayAccessExpression;
import com.neaterbits.compiler.ast.objects.expression.AssignmentExpression;
import com.neaterbits.compiler.ast.objects.expression.CastExpression;
import com.neaterbits.compiler.ast.objects.expression.ConditionalExpression;
import com.neaterbits.compiler.ast.objects.expression.GenericUnaryExpression;
import com.neaterbits.compiler.ast.objects.expression.NestedExpression;
import com.neaterbits.compiler.ast.objects.expression.arithemetic.binary.ArithmeticBinaryExpression;
import com.neaterbits.compiler.ast.objects.expression.arithemetic.unary.PostDecrementExpression;
import com.neaterbits.compiler.ast.objects.expression.arithemetic.unary.PostIncrementExpression;
import com.neaterbits.compiler.ast.objects.expression.arithemetic.unary.PreDecrementExpression;
import com.neaterbits.compiler.ast.objects.expression.arithemetic.unary.PreIncrementExpression;
import com.neaterbits.compiler.emit.EmitterState;
import com.neaterbits.compiler.emit.base.BaseInfixExpressionEmitter;
import com.neaterbits.compiler.types.operator.Arithmetic;
import com.neaterbits.compiler.types.operator.Assignment;
import com.neaterbits.compiler.types.operator.Bitwise;
import com.neaterbits.compiler.types.operator.IncrementDecrement;
import com.neaterbits.compiler.types.operator.Instantiation;
import com.neaterbits.compiler.types.operator.Logical;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.compiler.types.operator.OperatorVisitor;
import com.neaterbits.compiler.types.operator.Relational;
import com.neaterbits.compiler.types.operator.Scope;

public abstract class CLikeExpressionEmitter<T extends EmitterState> extends BaseInfixExpressionEmitter<T> {

	@Override
	public final Void onPreIncrement(PreIncrementExpression expression, T param) {

		param.append("++");
		
		emitExpression(expression.getExpression(), param);
		
		return null;
	}

	@Override
	public final Void onPreDecrement(PreDecrementExpression expression, T param) {

		param.append("--");

		emitExpression(expression.getExpression(), param);
		
		return null;
	}

	@Override
	public final Void onPostIncrement(PostIncrementExpression expression, T param) {

		emitExpression(expression.getExpression(), param);
		
		param.append("++");
		
		return null;
	}

	@Override
	public final Void onPostDecrement(PostDecrementExpression expression, T param) {

		emitExpression(expression.getExpression(), param);
		
		param.append("--");
		
		return null;
	}

	@Override
    public Void onGenericUnaryExpression(GenericUnaryExpression unaryExpression, T param) {

	    switch (unaryExpression.getOperator().getNotation()) {
	    case PREFIX:
	        param.append(getOperatorString(unaryExpression.getOperator()));
	        param.append(' ');
	        emitExpression(unaryExpression.getExpression(), param);
	        break;

	    case POSTFIX:
            emitExpression(unaryExpression.getExpression(), param);
            param.append(' ');
            param.append(getOperatorString(unaryExpression.getOperator()));
            break;

        default:
            throw new UnsupportedOperationException();
	    }
	    
	    return null;
    }

    private static final char getCArithmeticOperator(Arithmetic arithmetic) {
		final char operator;
		
		switch (arithmetic) {
		case PLUS: 		operator = '+'; break;
		case MINUS: 	operator = '-'; break;
		case MULTIPLY: 	operator = '*'; break;
		case DIVIDE: 	operator = '/'; break;
		case MODULUS: 	operator = '%'; break;
		
		default:
			throw new UnsupportedOperationException("Not an arithemetic operator: " + arithmetic);
		}

		return operator;
	}

	private static final String getCBitwiseOperator(Bitwise bitwise) {
		
		final String operator;
		
		switch (bitwise) {
		case AND: 					operator = "&"; break;
		case OR: 					operator = "|"; break;
		case XOR: 					operator = "^"; break;
		case LEFTSHIFT: 			operator = "<<"; break;
		case RIGHTSHIFT_SIGNED: 	operator = ">>"; break;
		
		default:
			throw new UnsupportedOperationException("Not a bitwise operator: " + bitwise);
		}

		return operator;
	}
	
	private static final String getCRelationalOperator(Relational relational) {
		final String operator;
		
		switch (relational) {
		case EQUALS:					operator = "=="; break;
		case NOT_EQUALS: 				operator = "!="; break;
		case LESS_THAN: 				operator = "<"; break;
		case LESS_THAN_OR_EQUALS: 		operator = "<="; break;
		case GREATER_THAN: 				operator = ">"; break;
		case GREATER_THAN_OR_EQUALS: 	operator = ">="; break;

		default:
			throw new UnsupportedOperationException("Not a relational operator: " + relational);
		}
		
		
		return operator;
	}
	
	private static final String getCLogicalOperator(Logical logical) {
		final String operator;
		
		switch (logical) {
		case AND: 	operator = "&&"; break;
		case OR: 	operator = "||"; break;
		case NOT: 	operator = "!"; break;
		
		default:
			throw new UnsupportedOperationException("Not a logical operator: " + logical);
		}

		return operator;
	}

	private static final String getCIncrementDecrementOperator(IncrementDecrement incrementDecrement) {
	    
        final String operator;
        
        switch (incrementDecrement) {
        case PRE_INCREMENT:
        case POST_INCREMENT:
            operator = "++";
            break;
            
        case PRE_DECREMENT:
        case POST_DECREMENT:
            operator = "--";
            break;
        
        default:
            throw new UnsupportedOperationException("Not a IncrementDecrement operator: " + incrementDecrement);
        }

        return operator;
    }

	private static final OperatorVisitor<Void, String> OPERATOR_TO_STRING_VISITOR = new OperatorVisitor<Void, String>() {
		
		@Override
		public String onRelational(Relational relational, Void param) {
			return getCRelationalOperator(relational);
		}
		
		@Override
		public String onBitwise(Bitwise bitwise, Void param) {
			return getCBitwiseOperator(bitwise);
		}
		
		@Override
		public String onArithmetic(Arithmetic arithmetic, Void param) {
			return "" + getCArithmeticOperator(arithmetic);
		}

		@Override
        public String onIncrementDecrement(IncrementDecrement incrementDecrement, Void param) {
		    return getCIncrementDecrementOperator(incrementDecrement);
		}

        @Override
		public String onLogical(Logical logical, Void param) {
			return getCLogicalOperator(logical);
		}

        @Override
        public String onInstantiation(Instantiation instantiation, Void param) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String onScope(Scope scope, Void param) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String onAssignment(Assignment assignment, Void param) {
            return "=";
        }
	};
	
	@Override
	protected String getOperatorString(Operator operator) {
		return operator.visit(OPERATOR_TO_STRING_VISITOR, null);
	}

	@Override
	public final Void onArithmeticBinary(ArithmeticBinaryExpression expression, T param) {
		
		final char operator = getCArithmeticOperator(expression.getOperator());

		emitExpression(expression.getLhs(), param);

		param.append(' ').append(operator).append(' ');

		return null;
	}

	
	@Override
	public Void onArrayAccessExpression(ArrayAccessExpression expression, T param) {

		emitExpression(expression.getArray(), param);
		
		param.append('[');
		
		emitExpression(expression.getIndex(), param);
		
		param.append(']');
		
		return null;
	}

	@Override
	public final Void onAssignment(AssignmentExpression expression, T param) {
		
		emitVariableReference(expression.getVariable(), param);

		param.append(" = ");

		emitExpression(expression.getExpression(), param);
		
		return null;
	}

	@Override
	public Void onNestedExpression(NestedExpression expression, T param) {
		
		param.append('(');
		
		emitExpression(expression.getExpression(), param);
		
		param.append(')');
		
		return null;
	}

	@Override
	public final Void onConditionalExpression(ConditionalExpression expression, T param) {
		
		emitExpression(expression.getPart1(), param);
		
		param.append(" ? ");
		
		emitExpression(expression.getPart2(), param);
		
		param.append(" : ");
		
		emitExpression(expression.getPart3(), param);
		
		return null;
	}

	@Override
	public final Void onCastExpression(CastExpression expression, T param) {

		param.append('(');
		
		emitTypeReference(expression.getCastType(), param);
		
		param.append(')');
		
		emitExpression(expression.getExpression(), param);
		
		return null;
	}
}

