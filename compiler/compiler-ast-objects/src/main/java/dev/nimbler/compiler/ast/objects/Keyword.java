package dev.nimbler.compiler.ast.objects;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.KEYWORD;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
