package com.neaterbits.ide.model.text.difftextmodel;

import com.neaterbits.ide.model.text.difftextmodel.GetLineIterator.TextGetLineState;
import com.neaterbits.ide.util.ui.text.LineDelimiter;
import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.TextBuilder;

final class GetLineIterator extends LineCountingIterator<TextGetLineState> {

	static class TextGetLineState extends LineCountingIterator.LinesCounterState {
		
		private final long lineIndex;
		private final Text initialText;
		private final TextBuilder textBuilder;

		private boolean hasAppended;
		
		TextGetLineState(long lineIndex, LineDelimiter lineDelimiter, Text initialText, LinesOffsets initialOffsets, TextBuilder textBuilder) {
			super(lineDelimiter, initialOffsets);
			
			this.lineIndex = lineIndex;
			this.initialText = initialText;
			this.textBuilder = textBuilder;
		}
		
		void append(Text text) {
			
			textBuilder.append(text);
			
			this.hasAppended = true;
		}
		
		boolean hasAppended() {
			return hasAppended;
		}
	}

	@Override
	boolean onDiffTextOffset(long offsetIntoWholeText, DiffTextOffset offset, long startLineInChunkRelativeToWholeText, TextGetLineState state) {
		
		System.out.println("## onDiffTextOffset startLine=" + startLineInChunkRelativeToWholeText);
		
		final boolean continueIteration;
		
		final long lastLineInChunkRelativeToWholeText =   startLineInChunkRelativeToWholeText
														+ offset.getChangeInNumberOfLines(state.lineDelimiter);
		
		if (state.lineIndex > lastLineInChunkRelativeToWholeText) {
			continueIteration = true;
		}
		else if (state.lineIndex >= startLineInChunkRelativeToWholeText
				&& state.lineIndex <= lastLineInChunkRelativeToWholeText) {
			
			System.out.println("## append");
		
			final long lineIndexInChunk = state.lineIndex - startLineInChunkRelativeToWholeText;
			
			final Text line = offset.getLine(lineIndexInChunk, state.lineDelimiter);
			
			if (line != null) {
				state.append(line);
			}

			continueIteration = true;
		}
		else {
			System.out.println("## exit");
			
			continueIteration = false;
		}
	
		return continueIteration;
	}

	@Override
	boolean onInitialModelText(
			long chunkOffsetIntoWholeText,
			long chunkOffsetIntoInitial,
			long lengthOfInitialTextChunk,
			long startLineInChunkRelativeToInitialText,
			long numLinesInInitialTextChunk,
			long startLineInChunkRelativeToWholeText,
			long lastLineInChunkRelativeToWholeText,
			TextGetLineState state) {

		System.out.println("## onInitialModelText offsetIntoInitial=" + chunkOffsetIntoInitial + ", startLine=" + startLineInChunkRelativeToWholeText
				+ ", lastLine=" + lastLineInChunkRelativeToWholeText + ", lineIndex=" + state.lineIndex);

		if (lastLineInChunkRelativeToWholeText < startLineInChunkRelativeToWholeText) {
			throw new IllegalStateException();
		}

		final boolean continueIteration;
		
		if (state.lineIndex > lastLineInChunkRelativeToWholeText) {
			continueIteration = true;
		}
		else if (state.lineIndex >= startLineInChunkRelativeToWholeText && state.lineIndex <= lastLineInChunkRelativeToWholeText) {
			
			final long lineIndexIntoChunk = state.lineIndex - startLineInChunkRelativeToWholeText;
			
			final long lineOffset = state.initialOffsets.getOffsetForLine(startLineInChunkRelativeToInitialText + lineIndexIntoChunk);
			
			final long nextLineIndex = startLineInChunkRelativeToInitialText + lineIndexIntoChunk + 1;
			
			final Text text;
			
			if (nextLineIndex >= state.initialOffsets.getNumLines()) {
				
				System.out.println("## onInitialModelText last line index=" + lineIndexIntoChunk + ", lineOffset="
							+ lineOffset + ", endIndex=" + (lineOffset + lengthOfInitialTextChunk));
					// Last line in text, just add remaining
				text = state.initialText.substring(
						Math.max(chunkOffsetIntoInitial, lineOffset),
						chunkOffsetIntoInitial + lengthOfInitialTextChunk);
			}
			else {
			
				final long offsetOfNextLine = state.initialOffsets.getOffsetForLine(nextLineIndex);
	
				System.out.println("## onInitialModelText prior line index=" + lineIndexIntoChunk + ", lineOffset=" + lineOffset + ", offsetOfNextLine=" + offsetOfNextLine);
	
				text = state.initialText.substring(
						Math.max(chunkOffsetIntoInitial, lineOffset),
						Math.min(lineOffset + lengthOfInitialTextChunk, offsetOfNextLine));
			}
			
			state.append(text);
			
			continueIteration = false;
		}
		else {
			continueIteration = false;
		}

		System.out.println("## continueIteration " + continueIteration);
		
		return continueIteration;
	}
}
