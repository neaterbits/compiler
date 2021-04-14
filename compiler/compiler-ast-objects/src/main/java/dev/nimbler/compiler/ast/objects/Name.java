package dev.nimbler.compiler.ast.objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;

public final class Name extends BaseASTElement {

	private final String text;
	
	public Name(Context context, String text) {
		super(context);
		
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
