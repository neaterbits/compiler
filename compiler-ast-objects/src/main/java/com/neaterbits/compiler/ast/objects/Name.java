package com.neaterbits.compiler.ast.objects;

import java.util.Objects;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class Name extends BaseASTElement {

	private final String text;
	
	public Name(Context context, String text) {
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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.NAME;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}

    @Override
    public String toString() {
        return "Name [text=" + text + "]";
    }
}