package dev.nimbler.ide.model.text.difftextmodel;

import java.util.function.Function;

import dev.nimbler.ide.util.ui.text.LineDelimiter;

/**
 * Iterator for retrieving offset for start of line from diff text model
 */
final class GetOffsetForLineIterator extends LineCountingIterator<GetOffsetForLineIterator.GetOffsetForLineState> {

	static class GetOffsetForLineState extends LineCountingIterator.LinesCounterState {
		
		private final long lineIndex;
		
		long offset;
		
		GetOffsetForLineState(long lineIndex, LineDelimiter lineDelimiter, LinesOffsets initialOffsets) {
			super(lineDelimiter, initialOffsets);
			
			this.lineIndex = lineIndex;
			this.offset = -1;
		}
	}

	private boolean findLineInChunk(
			long chunkOffsetIntoWholeText,
			long startLineInChunkRelativeToWholeText,
			long lastLineInChunkRelativeToWholeText,
			GetOffsetForLineState state,
			Function<Long, Long> getChunkOffsetOfLine) {

		final boolean continueIteration;
		
		if (state.lineIndex > lastLineInChunkRelativeToWholeText) {
			continueIteration = true;
		}
		else if (state.lineIndex >= startLineInChunkRelativeToWholeText && state.lineIndex <= lastLineInChunkRelativeToWholeText) {

			final long lineIndexIntoChunk = state.lineIndex - startLineInChunkRelativeToWholeText;

			System.out.println("## lineIndexIntoChunk=" + lineIndexIntoChunk + " from " + startLineInChunkRelativeToWholeText);
			
			final long chunkOffsetOfLine = getChunkOffsetOfLine.apply(lineIndexIntoChunk);
			
			if (chunkOffsetOfLine == -1) {
				state.offset = -1;
			}
			else {
				state.offset = chunkOffsetIntoWholeText + chunkOffsetOfLine;
			}
			
			continueIteration = false;
		}
		else {
			continueIteration = false;
		}
	
		return continueIteration;
	}
	
	@Override
	boolean onDiffTextOffset(long chunkOffsetIntoWholeText, DiffTextOffset chunk, long startLineInChunkRelativeToWholeText, GetOffsetForLineState state) {

		return findLineInChunk(
				chunkOffsetIntoWholeText,
				startLineInChunkRelativeToWholeText,
				chunk.getLastLineInChunk(startLineInChunkRelativeToWholeText, state.lineDelimiter),
				state,
				lineIndexIntoChunk -> chunk.getOffsetForLine(lineIndexIntoChunk, state.lineDelimiter));
		
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
			GetOffsetForLineState state) {
		
		return findLineInChunk(
				chunkOffsetIntoWholeText,
				startLineInChunkRelativeToWholeText,
				startLineInChunkRelativeToWholeText + numLinesInInitialTextChunk,
				state,
				lineIndexIntoChunk -> {

			System.out.println("## lineIndexIntoChunk " + lineIndexIntoChunk
					+ ", startLineInChunkRelativeToInitialText " + startLineInChunkRelativeToInitialText
					+ ", numLines=" + state.initialOffsets.getNumLines());
			
			final long lineIndexIntoInitial = startLineInChunkRelativeToInitialText + lineIndexIntoChunk;
			
			final long result;
			
			if (lineIndexIntoInitial >= state.initialOffsets.getNumLines()) {
				result = -1;
			}
			else {
				final long lineOffsetIntoInitial = state.initialOffsets.getOffsetForLine(lineIndexIntoInitial);

				result = lineOffsetIntoInitial - chunkOffsetIntoInitial;
			}
					
			return result;
		});
	}
}
