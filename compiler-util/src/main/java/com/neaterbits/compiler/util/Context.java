package com.neaterbits.compiler.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public final class Context {

	private final String file;
	private final int startLine;
	private final int startPosInLine;
	private final int startOffset;
	private final int endLine;
	private final int endPosInLine;
	private final int endOffset;
	private final String text;
	private final int tokenSequenceNo;

	public static Context makeTestContext() {
		return makeTestContext(-1);
	}
	
	public static Context makeTestContext(int tokenSequenceNo) {
		return new Context("", 0, 0, 0, 0, 0, 0, "", tokenSequenceNo);
	}

	public static <T> Context merge(Collection<T> elements, Function<T, Context> getContext) {
		
		if (elements.size() <= 1) {
			throw new IllegalArgumentException();
		}
		
		Context lower = null;
		Context upper = null;
		
		final StringBuilder sb = new StringBuilder();
		
		final Iterator<T> iter = elements.iterator();
		
		while (iter.hasNext()) {
			final Context context = getContext.apply(iter.next());
		
			if (lower == null) {
				lower = context;
			}
			else {
				sb.append(' ');
				
				if (!lower.getFile().equals(context.getFile())) {
					throw new IllegalArgumentException();
				}
			}
			
			upper = context;
			
			sb.append(context.getText());
		}
		
		if (lower.endOffset > upper.startOffset) {
			throw new IllegalArgumentException();
		}
		
		return new Context(
				lower.file,
				lower.startLine, lower.startPosInLine, lower.getStartOffset(), 
				upper.endLine, upper.endPosInLine, upper.endOffset,
				sb.toString(),
				lower.tokenSequenceNo);
	}
	
	public Context(String file, int startLine, int startPosInLine, int startOffset, int endLine, int endPos, int endOffset, String text, int tokenSequenceNo) {
		
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

	public int getEndOffset() {
		return endOffset;
	}
	
	public int getLength() {
		return getEndOffset() - getStartOffset() + 1;
	}
	
	public String getText() {
		return text;
	}

	public int getTokenSequenceNo() {
		return tokenSequenceNo;
	}

	@Override
	public String toString() {
		return "Context [file=" + file + ", startLine=" + startLine + ", startPosInLine=" + startPosInLine + ", endLine=" + endLine
				+ ", endPosInLine=" + endPosInLine + "]";
	}
}
