package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

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
