package com.neaterbits.compiler.antlr4;

final class ParserContextImpl implements EnterParserContext, ExitParserContext {
	private String text;
	private String ruleName;
	private int startLine;
	private int startColumn;
	private int endLine;
	private int endColumn;
	
	@Override
	public String getText() {
		return text;
	}

	void setText(String text) {
		this.text = text;
	}

	@Override
	public String getRuleName() {
		return ruleName;
	}

	void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	@Override
	public int getStartLine() {
		return startLine;
	}

	void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	@Override
	public int getStartColumn() {
		return startColumn;
	}

	void setStartColumn(int startColumn) {
		this.startColumn = startColumn;
	}

	@Override
	public int getEndLine() {
		return endLine;
	}

	void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	@Override
	public int getEndColumn() {
		return endColumn;
	}

	void setEndColumn(int endColumn) {
		this.endColumn = endColumn;
	}
}
