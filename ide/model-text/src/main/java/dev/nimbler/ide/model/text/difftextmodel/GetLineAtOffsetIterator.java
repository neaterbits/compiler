package dev.nimbler.ide.model.text.difftextmodel;

import java.util.function.Function;

import dev.nimbler.ide.util.ui.text.LineDelimiter;

/**
 * Iterator for computing line at offset from diff text model
 */

final class GetLineAtOffsetIterator extends LineCountingIterator<GetLineAtOffsetIterator.GetLineAtOffsetState> {

	static class GetLineAtOffsetState extends LineCountingIterator.LinesCounterState {

		private final long offset;
		
		private long line;
		
		GetLineAtOffsetState(long offset, LineDelimiter lineDelimiter, LinesOffsets initialOffsets) {
			super(lineDelimiter, initialOffsets);
		
			this.offset = offset;
			this.line = -1;
		}

		long getLine() {
			return line;
		}
	}

	private boolean findLine(
			long chunkOffsetIntoWholeText,
			long chunkLength,
			long startLineInChunkRelativeToWholeText,
			GetLineAtOffsetState state,
			Function<Long, Long> getLineIndexInChunk) {

		final boolean continueIteration;

		final long chunkLastOffsetIntoWholeText = chunkOffsetIntoWholeText + chunkLength - 1;
		
		if (state.offset > chunkLastOffsetIntoWholeText) {
			continueIteration = true;
		}
		else if (chunkOffsetIntoWholeText <= state.offset &&  chunkLastOffsetIntoWholeText >= state.offset) {
			// In this chunk
			final long chunkLine = getLineIndexInChunk.apply(state.offset - chunkOffsetIntoWholeText);
			
			state.line = startLineInChunkRelativeToWholeText + chunkLine;
			
			continueIteration = false;
		}
		else {
			continueIteration = false;
		}

		return continueIteration;
		
	}
	
	@Override
	boolean onDiffTextOffset(long offsetIntoWholeText, DiffTextOffset offset, long startLineInChunkRelativeToWholeText, GetLineAtOffsetState state) {

		return findLine(
				offsetIntoWholeText,
				offset.getNewLength(),
				startLineInChunkRelativeToWholeText,
				state,
				offsetIntoChunk -> offset.getLineAtOffset(offsetIntoChunk, state.lineDelimiter));
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
			GetLineAtOffsetState state) {


		return findLine(
				chunkOffsetIntoWholeText,
				lengthOfInitialTextChunk,
				startLineInChunkRelativeToWholeText,
				state,
				offsetIntoChunk -> {
					
					final long offsetIntoInitial = chunkOffsetIntoInitial + offsetIntoChunk;

					final long lineIndexIntoInitial = state.initialOffsets.getLineAtOffset(offsetIntoInitial);
					
					final long chunkLine = lineIndexIntoInitial - startLineInChunkRelativeToInitialText;
					
					return chunkLine;
				});
	}
}
