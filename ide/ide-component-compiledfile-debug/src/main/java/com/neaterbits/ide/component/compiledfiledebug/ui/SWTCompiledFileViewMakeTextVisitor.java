package com.neaterbits.ide.component.compiledfiledebug.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.compiler.model.common.SourceTokenVisitor;


final class SWTCompiledFileViewMakeTextVisitor implements SourceTokenVisitor {
	
	private final StringBuilder sb;
	private final long editorCursorOffset;

	private final List<Element> tokens;

	private int indent;
	
	private int stringBuilderLengthAtCursorOffset = -1;
	private int stringBuilderLineAtCursorOffset = 0;
	private Element foundCursorElement;
	
	private int foundLength;

	private static class Element {
		private final ISourceToken token;
		private final int textOffset;

		public Element(ISourceToken token, int textOffset) {
			
			Objects.requireNonNull(token);
			
			this.token = token;
			this.textOffset = textOffset;
		}
	}

			
	public SWTCompiledFileViewMakeTextVisitor(StringBuilder sb, long editorCursorOffset) {

		Objects.requireNonNull(sb);
		
		this.sb = sb;
		this.editorCursorOffset = editorCursorOffset;
		
		this.tokens = new ArrayList<>();
	}

	@Override
	public void onToken(ISourceToken token) {
		
		Objects.requireNonNull(token);
		
		for (int i = 0; i < indent; ++ i) {
			sb.append("  ");
		}
		
		if (stringBuilderLengthAtCursorOffset == -1) {
			if (token.getStartOffset() > editorCursorOffset) {
				stringBuilderLengthAtCursorOffset = sb.length();
			}
			
			++ stringBuilderLineAtCursorOffset;
		}

		
		sb.append(token.getTokenTypeDebugName())
			.append(" [").append(token.getStartOffset()).append(", ").append(token.getLength()).append("]\n");
	}

	@Override
	public void onPush(ISourceToken token) {
		
		Objects.requireNonNull(token);
		
		indent ++;
		
		if (foundCursorElement == null) {

			// Any earlier token found? If so clear since this is further towards a leaf token
			
			if (tokens.size() != indent - 1) {
				throw new IllegalStateException("mismatch " + tokens.size() + "/" + (indent - 1));
			}
			
			for (int i = 0; i < tokens.size() - 1; ++ i) {
				tokens.set(i, null);
			}
		
			final ISourceToken matchingToken;
			
			if (token.getStartOffset() <= editorCursorOffset && token.getStartOffset() + token.getLength() >= editorCursorOffset && !token.isPlaceholder()) {
				
				// System.out.println("## matching token " + token.getTokenTypeDebugName());
				
				matchingToken = token;
			}
			else {
				matchingToken = null;
			}
			
			tokens.add(matchingToken != null ? new Element(matchingToken, sb.length()) : null);
		}
	}
	
	ISourceToken getFoundCursorToken() {
		return foundCursorElement != null ? foundCursorElement.token : null;
	}

	int getFoundStringBuilderTextOffset() {
		return foundCursorElement != null ? foundCursorElement.textOffset : null;
	}


	int getStringBuilderLineAtCursorOffset() {
		return stringBuilderLineAtCursorOffset;
	}

	int getFoundLength() {
		return foundLength;
	}

	@Override
	public void onPop(ISourceToken token) {

		if (foundCursorElement == null) {
			if (indent > 0 && tokens.get(tokens.size() - 1) != null) {
				// leaf token
				foundCursorElement = tokens.get(tokens.size() - 1);
				
				foundLength = sb.length() - foundCursorElement.textOffset;
			}

			tokens.remove(tokens.size() - 1);
		}

		-- indent;

	}
}
