package dev.nimbler.ide.model.text.difftextmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import dev.nimbler.ide.model.text.PosEdit;
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

	private static final boolean DEBUG = false;
	
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
	DiffTextOffsets(List<PosEdit> edits, LinesOffsets initialOffsets) {
		
		this();
		
		edits.forEach(edit -> applyTextEditWithMerge(edit, initialOffsets));
	}
	
	// For unit tests
	UnmodifiableSortedArray<DiffTextOffset> getOffsetArray() {

	    return offsets;
	}

    /**
     * Apply a new text edit to the diff text offsets and update state model
     * 
     * @param edit the {@link PosEdit} to apply
     * @param initialOffsets the line offsets of the initial text
     */
    void applyTextEditWithMerge(PosEdit edit, LinesOffsets initialOffsets) {
        
        Objects.requireNonNull(edit);
        Objects.requireNonNull(initialOffsets);

        boolean doneMerging = false;

        // The merging DiffTextOffset, we should always be able to merge to just one item
        DiffTextOffset lastMerged = null;

        long curSortedArrayStartPos = 0;
        long lastSortedArrayStartPos = DiffTextOffset.NO_DISTANCE;
        
        final TextEdit textEdit = edit.getTextEdit();
        
        if (offsets.isEmpty()) {
            offsets.insertAt(0, new DiffTextOffset(textEdit, edit.getStartPos()));
        }
        else {
            // A list of diff texts to remove
            final List<DiffTextOffset> mergedOffsetsToReplace = new ArrayList<>();
            
            int indexIntoSortedArrayOfOffsetsToReplace = -1;
            int curSortedArrayIndex = -1;

            for (int i = 0; i < offsets.length() && !doneMerging; ++ i) {
                
                final DiffTextOffset priorEditOffset = offsets.get(i);

                if (i == 0 && priorEditOffset.getOriginalStartPos() > 0) {
                    curSortedArrayStartPos = priorEditOffset.getOriginalStartPos();
                }

                // Merge from either user edit or last merge (which user edit has been merged with)
                final TextEdit curEditToMerge;
                final long curEditToMergeStartPos;

                if (lastMerged == null) {
                    curEditToMerge = edit.getTextEdit();
                    curEditToMergeStartPos = edit.getStartPos();
                }
                else {
                    curEditToMerge = lastMerged.getEdit();

                    // lastMerged is created from merging other entries and
                    // therefore has up to date originalStartPos
                    curEditToMergeStartPos = lastMerged.getOriginalStartPos();
                }

                final DiffTextOffset merged = merge(
                        curEditToMerge,
                        curEditToMergeStartPos,
                        priorEditOffset,
                        curSortedArrayStartPos);

                if (merged != null) {
                    
                    // Merge was doable so can replace in sorted array
                    if (lastMerged == null) {
                        // Started merging so cache where to replace in sorted array
                        indexIntoSortedArrayOfOffsetsToReplace = i;
                    }

                    mergedOffsetsToReplace.add(priorEditOffset);
                    
                    // Merge with the merged DiffTextOffset
                    lastMerged = merged;
                }
                else {
                    // Not mergeable so done merging, if was able to merge at all
                    doneMerging = true;
                    curSortedArrayIndex = i;
                }

                lastSortedArrayStartPos = curSortedArrayStartPos;

                curSortedArrayStartPos += priorEditOffset.getEdit().getNewLength();
                
                if (i < offsets.length() - 1) {
                    curSortedArrayStartPos += priorEditOffset.getDistanceToNextTextEdit();
                }
            }

            // Done merging, now replace the merged offsets
            if (lastMerged != null) {

                // Was able to merge
                offsets.replace(
                        indexIntoSortedArrayOfOffsetsToReplace,
                        mergedOffsetsToReplace.size(),
                        Arrays.asList(lastMerged));
            }
            else {
                // Not able to merge so just add at index
                addNonMerged(
                        edit,
                        curSortedArrayIndex,
                        lastSortedArrayStartPos,
                        curSortedArrayStartPos);
            }
        }
    }

    private void addNonMerged(
            PosEdit edit,
            int indexIntoSortedArrayOfOffsetsToReplace,
            long lastSortedArrayStartPos,
            long curSortedArrayStartPos) {
        
        final DiffTextOffset offset;

        final long editStartPos = edit.getStartPos();
        final TextEdit textEdit = edit.getTextEdit();

        if (indexIntoSortedArrayOfOffsetsToReplace >= offsets.length()) {
            throw new IllegalStateException();
        }
        else if (indexIntoSortedArrayOfOffsetsToReplace == offsets.length() - 1) {
            // Append to array
            offset = new DiffTextOffset(edit.getTextEdit(), editStartPos);
        }
        else {
            
            if (curSortedArrayStartPos <= editStartPos) {
                throw new IllegalStateException();
            }

            // Must compute distance to next entry for this one
            offset = new DiffTextOffset(
                    textEdit,
                    editStartPos,
                    curSortedArrayStartPos - editStartPos);
        }
        
        if (indexIntoSortedArrayOfOffsetsToReplace > 0) {
            // Update distance to next for last entry
            
            if (lastSortedArrayStartPos == DiffTextOffset.NO_DISTANCE) {
                throw new IllegalStateException();
            }
            
            final int lastIndex = indexIntoSortedArrayOfOffsetsToReplace - 1;
            
            final DiffTextOffset lastOffset = offsets.get(lastIndex);
            
            if (editStartPos <= lastSortedArrayStartPos) {
                throw new IllegalStateException();
            }
            
            final DiffTextOffset lastOffsetUpdate = new DiffTextOffset(
                    lastOffset.getEdit(),
                    lastOffset.getOriginalStartPos(),
                    editStartPos - lastSortedArrayStartPos);
            
            offsets.set(lastIndex, lastOffsetUpdate);
        }

        offsets.insertAt(indexIntoSortedArrayOfOffsetsToReplace, offset);
    }
    
    private DiffTextOffset merge(
            
            // Current edit to merge, i.e. user edit or user edit merged with other edits
            TextEdit curEditToMerge,
            long curEditToMergeStartPos,
            
            // Edit to merge with
            DiffTextOffset fromSortedArray,
            long fromSortedArrayStartPos) {

        System.out.println("## merge " + curEditToMerge + "/" + curEditToMergeStartPos
                + " with " + fromSortedArray + "/" + fromSortedArrayStartPos);
        
        final DiffTextOffset merged;
        
        if (curEditToMergeStartPos < fromSortedArrayStartPos) {

            // Must increase fromSortedArrayStartPos because the new merge would move it
            // eg. adding the text "123" at pos 0, then add "321" at the same pos would merge [0, "321"] to [2, "123"]

            merged = mergeCurEditBeforePrior(
                    curEditToMerge,
                    curEditToMergeStartPos,
                    fromSortedArray,
                    fromSortedArrayStartPos + curEditToMerge.getNewLength() - curEditToMerge.getOldLength());
        }
        else {
            
            merged = mergePriorEditBeforeCur(curEditToMerge, curEditToMergeStartPos, fromSortedArray, fromSortedArrayStartPos);
        }

        return merged;
    }
    
    private static DiffTextOffset mergeCurEditBeforePrior(
            // Current edit to merge, i.e. user edit or user edit merged with other edits
            TextEdit curEditToMerge,
            long curEditToMergeStartPos,
            
            // Edit to merge with
            DiffTextOffset fromSortedArray,
            long fromSortedArrayStartPos) {
        
        final DiffTextOffset merged;
        
        if (curEditToMerge.isMergeable(
                curEditToMergeStartPos,
                fromSortedArray.getEdit(),
                fromSortedArrayStartPos)) {

            System.out.format("## mergeable cur %s -> from %s\n",
                    textEditString(curEditToMergeStartPos, curEditToMerge),
                    textEditString(fromSortedArrayStartPos, fromSortedArray.getEdit()));

            if (curEditToMergeStartPos > fromSortedArrayStartPos) {
                throw new IllegalStateException();
            }

            final TextEdit mergedTextEdit = TextEdit.merge(curEditToMerge, curEditToMergeStartPos,
                    fromSortedArray.getEdit(), fromSortedArrayStartPos);

            if (fromSortedArray.isLastTextEdit()) {

                merged = new DiffTextOffset(mergedTextEdit, curEditToMergeStartPos);
            } else {
                final long updatedDistanceToNext = fromSortedArray.getDistanceToNextTextEdit()
                        + (mergedTextEdit.getNewLength() - mergedTextEdit.getOldLength())
                        + (fromSortedArrayStartPos - curEditToMergeStartPos);

                merged = new DiffTextOffset(mergedTextEdit, curEditToMergeStartPos, updatedDistanceToNext);
            }
        }
        else if (fromSortedArray.getEdit().isMergeable(
                fromSortedArrayStartPos,
                curEditToMerge,
                curEditToMergeStartPos)) {
            throw new IllegalStateException();
        }
        else {
            merged = null;
        }

        return merged;
    }

    private static DiffTextOffset mergePriorEditBeforeCur(
            // Current edit to merge, i.e. user edit or user edit merged with other edits
            TextEdit curEditToMerge,
            long curEditToMergeStartPos,
            
            // Edit to merge with
            DiffTextOffset fromSortedArray,
            long fromSortedArrayStartPos) {
        
        final DiffTextOffset merged;
        
        if (fromSortedArray.getEdit().isMergeable(
                fromSortedArrayStartPos,
                curEditToMerge,
                curEditToMergeStartPos)) {

            System.out.format("## mergeable from %s -> cur %s\n",
                    textEditString(fromSortedArrayStartPos, fromSortedArray.getEdit()),
                    textEditString(curEditToMergeStartPos, curEditToMerge));

            final TextEdit mergedTextEdit = TextEdit.merge(fromSortedArray.getEdit(), fromSortedArrayStartPos,
                    curEditToMerge, curEditToMergeStartPos);

            if (fromSortedArray.isLastTextEdit()) {
                merged = new DiffTextOffset(mergedTextEdit, fromSortedArrayStartPos);
            } else {
                final long updatedDistanceToNext = fromSortedArray.getDistanceToNextTextEdit()
                        + mergedTextEdit.getNewLength() - mergedTextEdit.getOldLength();

                merged = new DiffTextOffset(mergedTextEdit, fromSortedArrayStartPos, updatedDistanceToNext);
            }

        }
        else if (curEditToMerge.isMergeable(
                curEditToMergeStartPos,
                fromSortedArray.getEdit(),
                fromSortedArrayStartPos)) {
         
            throw new IllegalStateException();
        }
        else {
            merged = null;
        }
        
        return merged;
    }

    private static String textEditString(long startPos, TextEdit textEdit) {

        return String.format("[%d, '%s' -> '%s']",
                startPos,
                textEdit.getOldText().asString(),
                textEdit.getNewText().asString());
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
		}
		else {
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

		final long initialStartPos = initialOffset.getOriginalStartPos();
		
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
				
				final TextEdit textEdit = offset.getEdit();
				
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

				if (offset.isLastTextEdit()) {
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

    @Override
    public String toString() {
        return "DiffTextOffsets [offsets=" + offsets + "]";
    }
}
