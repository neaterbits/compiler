package com.neaterbits.compiler.ast.parser;

import java.util.Objects;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.util.parse.ParseLogger;

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
		
		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}
		
		this.name = name;
	}
}
