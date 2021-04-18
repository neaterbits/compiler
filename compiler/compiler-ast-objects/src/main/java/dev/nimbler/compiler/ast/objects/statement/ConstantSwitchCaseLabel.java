package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ConstantSwitchCaseLabel extends SwitchCaseLabel {

	private final ASTSingle<Expression> constant;

	public ConstantSwitchCaseLabel(Context context, Keyword keyword, Expression constant) {
		super(context, keyword);

		Objects.requireNonNull(constant);
		
		this.constant = makeSingle(constant);
	}

	public Expression getConstant() {
		return constant.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CONSTANT_SWITCH_CASE_LABEL;
	}

	@Override
	public <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param) {
		return visitor.onConstant(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		super.doRecurse(recurseMode, iterator);
		
		doIterate(constant, recurseMode, iterator);
	}
}
