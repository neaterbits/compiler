package com.neaterbits.compiler.common.parser;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.log.ParseLogger;

public class NamedListStackEntry<T extends BaseASTElement> extends ListStackEntry<T> {

	private String name;

	public NamedListStackEntry(ParseLogger parseLogger, String name) {
		super(parseLogger);

		this.name = name;
	}

	public NamedListStackEntry(ParseLogger parseLogger) {
		super(parseLogger);

		this.name = null;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		Objects.requireNonNull(name);
		
		this.name = name;
	}
}
