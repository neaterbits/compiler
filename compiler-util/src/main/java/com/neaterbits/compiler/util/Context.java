package com.neaterbits.compiler.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

public interface Context {

	public static Context makeTestContext() {
		return makeTestContext(-1);
	}
	
	public static Context makeTestContext(int tokenSequenceNo) {
		return new ImmutableContext("", 0, 0, 0, 0, 0, 0, "", tokenSequenceNo);
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
		
		if (lower.getEndOffset() > upper.getStartOffset()) {
			throw new IllegalArgumentException();
		}
		
		return new ImmutableContext(
				lower.getFile(),
				lower.getStartLine(), lower.getStartPosInLine(), lower.getStartOffset(), 
				upper.getEndLine(), upper.getEndPosInLine(), upper.getEndOffset(),
				sb.toString(),
				lower.getTokenSequenceNo());
	}
	
	String getFile();
	
	int getStartLine();
	
	int getStartPosInLine();
	
	int getStartOffset();
	
	int getEndLine();
	
	int getEndPosInLine();
	
	int getEndOffset();
	
	int getLength();
	
	String getText();
	
	int getTokenSequenceNo();

}
