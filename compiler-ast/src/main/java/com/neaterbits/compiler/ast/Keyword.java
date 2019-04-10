package com.neaterbits.compiler.ast;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;

// Generic placeholder for keywords, useful for syntax highlighting for Context
public final class Keyword extends BaseASTElement {

	private final String text;
	
	public Keyword(Context context, String text) {
		super(context);
		
		Objects.requireNonNull(text);
		
		if (!text.trim().equals(text)) {
			throw new IllegalArgumentException();
		}
		
		if (text.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
