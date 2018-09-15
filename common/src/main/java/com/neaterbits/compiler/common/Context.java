package com.neaterbits.compiler.common;

public final class Context {

	private final String file;
	private final int startLine;
	private final int startPos;
	private final int endLine;
	private final int endPos;

	public Context(String file, int startLine, int startPos, int endLine, int endPos) {
		this.file = file;
		this.startLine = startLine;
		this.startPos = startPos;
		this.endLine = endLine;
		this.endPos = endPos;
	}

	public String getFile() {
		return file;
	}

	public int getStartLine() {
		return startLine;
	}

	public int getStartPos() {
		return startPos;
	}

	public int getEndLine() {
		return endLine;
	}

	public int getEndPos() {
		return endPos;
	}

	@Override
	public String toString() {
		return "Context [file=" + file + ", startLine=" + startLine + ", startPos=" + startPos + ", endLine=" + endLine
				+ ", endPos=" + endPos + "]";
	}
}
