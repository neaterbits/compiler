package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
