package dev.nimbler.ide.model.text.difftextmodel;

import java.util.List;
import java.util.Objects;

import dev.nimbler.ide.model.text.TextEdit;
import dev.nimbler.ide.model.text.difftextmodel.CountingIterator.CounterState;
import dev.nimbler.ide.model.text.difftextmodel.GetLineAtOffsetIterator.GetLineAtOffsetState;
import dev.nimbler.ide.model.text.difftextmodel.GetLineIterator.TextGetLineState;
import dev.nimbler.ide.model.text.difftextmodel.GetOffsetForLineIterator.GetOffsetForLineState;
import dev.nimbler.ide.model.text.difftextmodel.GetTextRangeIterator.GetTextRangeState;
import dev.nimbler.ide.model.text.difftextmodel.LineCountingIterator.LinesCounterState;
import dev.nimbler.ide.util.ui.text.CharText;
import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.TextBuilder;

/**
 * Stores diff text edits in offset order, i.e. edits earlier in the edited text
 * appear earlier.
 * 
 */
class DiffTextOffsets {

	private static final boolean DEBUG = true;
	
	private SortedArray<DiffTextOffset> offsets;
	
	DiffTextOffsets() {
		this.offsets = new SortedArray<>(DiffTextOffset.class);
	}

	/**
	 * Create a {@link DiffTextOffsets} from initial edits an line offsets.
	 * 
	 * @param edits initial edits
	 * @param initialOffsets line offsets into initial text
	 */
	DiffTextOffsets(List<TextEdit> edits, LinesOffsets initialOffsets) {
		
		this();
		
		edits.forEach(edit -> applyTextEdit(edit, initialOffsets));
	}

	/**
	 * Apply a new text edit to the diff text offsets and update state model
	 * 
	 * @param edit the {@link TextEdit} to apply
	 * @param initialOffsets the line offsets of the initial text
	 */
	void applyTextEdit(TextEdit edit, LinesOffsets initialOffsets) {
		
		Objects.requireNonNull(edit);
		Objects.requireNonNull(initialOffsets);

		boolean applied = false;

		long curPos = 0;
		
		if (offsets.isEmpty()) {
			offsets.insertAt(0, new DiffTextOffset(edit, -1));
		}
		else {
			for (int i = 0; i < offsets.length() && !applied; ++ i) {
				
				final DiffTextOffset priorEdit = offsets.get(i);
				
				if (i == 0 && priorEdit.getTextEdit().getStartPos() > 0) {
					curPos = priorEdit.getTextEdit().getStartPos();
				}
				
				final ApplyTextEditResult applyResult = DiffTextOffsetIntersection.applyTextEdit(edit, i, curPos, priorEdit);
				
				switch (applyResult.getProcessResult()) {
				case COMPLETELY:
					// Applied into model completely
					System.out.println("## applied");
					applyResult.applyToArray(offsets);
					applied = true;
					break;
					
				case PARTLY:
					throw new UnsupportedOperationException();
					
				case NONE:
					break;
					
				default:
					throw new UnsupportedOperationException();
				}
				
				curPos += priorEdit.getNewLength() + priorEdit.getDistanceToNextTextEdit();
			}
		}
	}

	/**
	 * Retrieve complete text by applying all texts to an initial text.
	 * 
	 * @param curTextLength the current complete length
	 * @param initialText the initial text we are creating a diff against
	 * @param initialOffsets the initial text line offsets
	 * 
	 * @return text with all diffs applied
	 */
	Text getText(long curTextLength, Text initialText, LinesOffsets initialOffsets) {

		final Text result;
		
		if (offsets.isEmpty()) {
			result = initialText;

			System.out.println("## has no offsets " + offsets);
		}
		else {
		
			System.out.println("## has offsets " + offsets + " length " + curTextLength);

			final TextBuilder textBuilder = new CharText(curTextLength);
			
			iterateOffsets(curTextLength, initialOffsets, new GetTextIterator.GetTextState(initialText, textBuilder), new GetTextIterator());
			
			result = textBuilder.toText();
		}

		return result;
	}
	
	

	Text getTextRange(long start, long length, long curTextLength, Text initialText, LinesOffsets initialOffsets) {

		final Text result;
		
		if (offsets.isEmpty()) {
			result = initialText.substring(start, start + length);
		}
		else {
		
			final TextBuilder textBuilder = new CharText(length);
		
			final GetTextRangeState state = new GetTextRangeState(start, length, initialText, textBuilder);
			
			iterateOffsets(curTextLength, initialOffsets, state, new GetTextRangeIterator());
			
			result = textBuilder.toText();
		}
		
		return result;
	}
	
	private <T> void iterateOffsets(long curTextLength, LinesOffsets initialOffsets, T state, DiffTextOffsetsIterator<T> iterator) {

		if (DEBUG) {
			System.out.println("ENTER iterateOffsets curTextLength=" + curTextLength);
		}
		
		if (offsets.isEmpty()) {
			throw new IllegalStateException();
		}

		long curPos;
		
		long initialTextPos;
		
		final DiffTextOffset initialOffset = offsets.get(0);
		
		boolean continueIteration = true;

		final long initialStartPos = initialOffset.getTextEdit().getStartPos();
		
		// if not a diff at start pos, then get that from initial text
		if (initialStartPos > 0L) {
			
			curPos = initialStartPos;
			initialTextPos = initialStartPos;

			continueIteration = iterator.onInitialModelText(0L, 0L, initialStartPos, state);

			if (DEBUG) {
				System.out.println("found initial start pos curPos=" + curPos);
			}

		}
		else {
			curPos = 0L;
			initialTextPos = 0L;
		}
		
		if (continueIteration) {
			
			for (int i = 0; i < offsets.length(); ++ i) {
				
				final DiffTextOffset offset = offsets.get(i);
				
				final TextEdit textEdit = offset.getTextEdit();
				
				if (!iterator.onDiffTextOffset(curPos, offset, state)) {
					break;
				}
				
				// Skipping in initial text that has been removed
				initialTextPos += textEdit.getOldLength();

				if (DEBUG) {
					System.out.println("added " + textEdit.getOldLength() + " to initialTextPos, updated to " + initialTextPos);
				}

				curPos += textEdit.getNewLength();

				if (DEBUG) {
					System.out.println("added " + textEdit.getNewLength() + " to curPos, updated to " + curPos);
				}

				final long lengthOfInitialText;

				if (offset.getDistanceToNextTextEdit() < 0) {
					lengthOfInitialText = curTextLength - curPos;

					if (DEBUG) {
						System.out.println("lengthOfInitialText " + lengthOfInitialText + " from curTextLength=" + curTextLength + ", curPos=" + curPos);
					}

				}
				else {
					lengthOfInitialText = offset.getDistanceToNextTextEdit();

					if (DEBUG) {
						System.out.println("lengthOfInitialText " + lengthOfInitialText + " from offset=" + offset.getDistanceToNextTextEdit());
					}
				}
				
				if (!iterator.onInitialModelText(curPos, initialTextPos, lengthOfInitialText, state)) {
					break;
				}
			}
		}

		if (DEBUG) {
			System.out.println("EXIT iterateOffsets curPos=" + curPos);
		}
	}
	
	long getOffsetForLine(long lineIndex, long curTextLength, LineDelimiter lineDelimiter, LinesOffsets initialOffsets) {

		if (lineIndex < 0) {
			throw new IllegalArgumentException();
		}

		final long result;
		
		if (offsets.isEmpty()) {
			if (lineIndex >= initialOffsets.getNumLines()) {
				result = -1L;
			}
			else {
				result = initialOffsets.getOffsetForLine(lineIndex);
			}
		}
		else {
			final GetOffsetForLineState offsetForLineState = new GetOffsetForLineState(lineIndex, lineDelimiter, initialOffsets);
			
			iterateOffsets(curTextLength, initialOffsets, offsetForLineState, new GetOffsetForLineIterator());
			
			result = offsetForLineState.offset;
		}
		
		return result;
	}

	long getLineCount(long curTextLength, LineDelimiter lineDelimiter, LinesOffsets initialOffsets) {

		final long result;
		
		if (offsets.isEmpty()) {
			result = initialOffsets.getNumLines();
		}
		else {
			final LinesCounterState counter = new LinesCounterState(lineDelimiter, initialOffsets);
			
			iterateOffsets(curTextLength, initialOffsets, counter, new LineCountingIterator<>());
			
			result = counter.getCounter();
		}

		return result;
	}

	long getLineAtOffset(long offset, long curTextLength, LineDelimiter lineDelimiter, LinesOffsets initialOffsets) {

		final long result;
		
		if (offsets.isEmpty()) {
			result = initialOffsets.getLineAtOffset(offset);
		}
		else {
			
			final GetLineAtOffsetState state = new GetLineAtOffsetState(offset, lineDelimiter, initialOffsets);
			
			iterateOffsets(curTextLength, initialOffsets, state, new GetLineAtOffsetIterator());
			
			result = state.getLine();
		}
		
		return result;
	}
	
	Text getLine(long lineIndex, long curTextLength, LineDelimiter lineDelimiter, Text initialText, LinesOffsets initialOffsets) {
		
		final Text result;
		
		if (offsets.isEmpty()) {
			
			if (lineIndex >= initialOffsets.getNumLines()) {
				result = null;
			}
			else {
				
				final long lineOffset = initialOffsets.getOffsetForLine(lineIndex);
				
				result = initialText.substring(
						lineOffset,
						lineOffset + initialOffsets.getLengthOfLineWithAnyNewline(lineIndex));
			}
		}
		else {
		
			final TextBuilder textBuilder = new CharText();
			
			final TextGetLineState state = new TextGetLineState(lineIndex, lineDelimiter, initialText, initialOffsets, textBuilder);
			
			iterateOffsets(curTextLength, initialOffsets, state, new GetLineIterator());
			
			result = state.hasAppended() ? textBuilder.toText() : null;
		}

		return result;
	}
	
	long getCharCount(long curTextLength, LinesOffsets initialOffsets) {

		final long result;
		
		if (offsets.isEmpty()) {
			result = initialOffsets.getTextLength();
		}
		else {
		
			final CounterState counter = new CounterState();
	
			iterateOffsets(curTextLength, initialOffsets, counter, new GetCharCountIterator());
			
			result = counter.getCounter();
		}
		
		return result;
	}
}
