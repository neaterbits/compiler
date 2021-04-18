package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class DefaultSwitchCaseLabel extends SwitchCaseLabel {

	public DefaultSwitchCaseLabel(Context context, Keyword keyword) {
		super(context, keyword);
		
		Objects.requireNonNull(keyword);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.DEFAULT_SWITCH_CASE_LABEL;
	}

	@Override
	public <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param) {
		return visitor.onDefault(this, param);
	}
}
