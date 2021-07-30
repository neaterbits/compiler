package dev.nimbler.ide.model.text.difftextmodel;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.LineDelimiter;

/**
 * Base class for iterators that count lines.
 * 
 * @param <T> type of iterator state object.
 */
class LineCountingIterator<T extends LineCountingIterator.LinesCounterState> extends CountingIterator<T> {

	static class LinesCounterState extends CounterState {

		final LineDelimiter lineDelimiter;
		final LinesOffsets initialOffsets;
		
		LinesCounterState(LineDelimiter lineDelimiter, LinesOffsets initialOffsets) {

			Objects.requireNonNull(lineDelimiter);
			Objects.requireNonNull(initialOffsets);
			
			this.lineDelimiter = lineDelimiter;
			this.initialOffsets = initialOffsets;
		}
	}

	boolean onDiffTextOffset(long chunkOffsetIntoWholeText, DiffTextOffset chunk, long startLineInChunkRelativeToWholeText, T state) {
		return true;
	}
	
	boolean onInitialModelText(
			long chunkOffsetIntoWholeText,
			long chunkOffsetIntoInitial,
			long lengthOfInitialTextChunk,
			long startLineInChunkRelativeToInitialText,
			long numLinesInInitialTextChunk,
			long startLineInChunkRelativeToWholeText,
			long lastLineInChunkRelativeToWholeText,
			T state) {
		return true;
	}

	@Override
	public final boolean onDiffTextOffset(long offsetIntoWholeText, DiffTextOffset offset, T state) {

		System.out.println("## onDiffTextOffset " + offsetIntoWholeText+ "/" + offset.getChangeInNumberOfLines(state.lineDelimiter));

		final boolean continueIteration = onDiffTextOffset(offsetIntoWholeText, offset, state.getCounter(), state);
		
		state.addToCounter(offset.getChangeInNumberOfLines(state.lineDelimiter));
		
		return continueIteration;
	}

	@Override
	public final boolean onInitialModelText(long offsetIntoWholeText, long offsetIntoInitial, long lengthOfInitialText, T state) {

		System.out.println("## onInitialModeText " + offsetIntoInitial + "/" + lengthOfInitialText + "/" + state.initialOffsets.getTextLength());
		
		final long startLineIndexInInitial = state.initialOffsets.getLineAtOffset(offsetIntoInitial);
		
		System.out.println("## startLineIndexInInitial=" + startLineIndexInInitial);
		
		final long endOfInitialPos = offsetIntoInitial + lengthOfInitialText - 1;
		
		final long lastLineIndexInInitial = state.initialOffsets.getLineAtOffset(endOfInitialPos);
		
		if (startLineIndexInInitial < 0) {
			throw new IllegalStateException();
		}
		
		if (lastLineIndexInInitial < 0) {
			throw new IllegalStateException();
		}
		
		if (lastLineIndexInInitial < startLineIndexInInitial) {
			throw new IllegalStateException();
		}
		
		final long numLines = lastLineIndexInInitial - startLineIndexInInitial + 1;
		
		final boolean continueIteration = onInitialModelText(
				offsetIntoWholeText,
				offsetIntoInitial,
				lengthOfInitialText,
				startLineIndexInInitial,
				numLines,
				state.getCounter(),
				state.getCounter() + numLines - 1,
				state);
		
		state.addToCounter(numLines);
		
		return continueIteration;
	}
}
