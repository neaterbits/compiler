package com.neaterbits.compiler.util;

public final class Context {

	private final String file;
	private final int startLine;
	private final int startPosInLine;
	private final int startOffset;
	private final int endLine;
	private final int endPosInLine;
	private final int endOffset;
	private final String text;

	public static Context makeTestContext() {
		return new Context("", 0, 0, 0, 0, 0, 0, "");
	}
	
	public Context(String file, int startLine, int startPosInLine, int startOffset, int endLine, int endPos, int endOffset, String text) {
		
		this.file = file;
		this.startLine = startLine;
		this.startPosInLine = startPosInLine;
		this.startOffset = startOffset;
		this.endLine = endLine;
		this.endPosInLine = endPos;
		this.endOffset = endOffset;
		this.text = text;
	}

	public String getFile() {
		return file;
	}

	public int getStartLine() {
		return startLine;
	}

	public int getStartPosInLine() {
		return startPosInLine;
	}
	
	public int getStartOffset() {
		return startOffset;
	}

	public int getEndLine() {
		return endLine;
	}

	public int getEndPosInLine() {
		return endPosInLine;
	}

	public String getText() {
		return text;
	}

	public int getEndOffset() {
		return endOffset;
	}

	@Override
	public String toString() {
		return "Context [file=" + file + ", startLine=" + startLine + ", startPosInLine=" + startPosInLine + ", endLine=" + endLine
				+ ", endPosInLine=" + endPosInLine + "]";
	}
}
