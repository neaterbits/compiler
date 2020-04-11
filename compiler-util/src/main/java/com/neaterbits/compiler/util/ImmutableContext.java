package com.neaterbits.compiler.util;

public class ImmutableContext implements Context {

	private final String file;
	private final int startLine;
	private final int startPosInLine;
	private final int startOffset;
	private final int endLine;
	private final int endPosInLine;
	private final int endOffset;
	private final String text;
	private final int tokenSequenceNo;
	
	public ImmutableContext(String file, int startLine, int startPosInLine, int startOffset, int endLine, int endPos, int endOffset, String text, int tokenSequenceNo) {
		
		this.file = file;
		this.startLine = startLine;
		this.startPosInLine = startPosInLine;
		this.startOffset = startOffset;
		this.endLine = endLine;
		this.endPosInLine = endPos;
		this.endOffset = endOffset;
		this.text = text;
		this.tokenSequenceNo = tokenSequenceNo;
	}
	
	public ImmutableContext(Context context) {
	    this(
	            context.getFile(),
	            context.getStartLine(),
	            context.getStartPosInLine(),
	            context.getStartOffset(),
	            context.getEndLine(),
	            context.getEndPosInLine(),
	            context.getEndOffset(),
	            context.getText(),
	            context.getTokenSequenceNo());
	}

	@Override
	public String getFile() {
		return file;
	}

	@Override
	public int getStartLine() {
		return startLine;
	}

	@Override
	public int getStartPosInLine() {
		return startPosInLine;
	}
	
	@Override
	public int getStartOffset() {
		return startOffset;
	}

	@Override
	public int getEndLine() {
		return endLine;
	}

	@Override
	public int getEndPosInLine() {
		return endPosInLine;
	}

	@Override
	public int getEndOffset() {
		return endOffset;
	}
	
	@Override
	public int getLength() {
		return getEndOffset() - getStartOffset() + 1;
	}
	
	@Override
	public String getText() {
		return text;
	}

	@Override
	public int getTokenSequenceNo() {
		return tokenSequenceNo;
	}

	@Override
	public String toString() {
		return "ImmutableContext [file=" + file + ", startLine=" + startLine + ", startPosInLine=" + startPosInLine + ", endLine=" + endLine
				+ ", endPosInLine=" + endPosInLine + "]";
	}
}
