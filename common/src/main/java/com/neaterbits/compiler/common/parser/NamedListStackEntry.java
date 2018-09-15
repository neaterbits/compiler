package com.neaterbits.compiler.common.parser;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.BaseASTElement;

public class NamedListStackEntry<T extends BaseASTElement> extends ListStackEntry<T> {

	private String name;

	public NamedListStackEntry(String name) {
		this.name = name;
	}

	public NamedListStackEntry() {
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
