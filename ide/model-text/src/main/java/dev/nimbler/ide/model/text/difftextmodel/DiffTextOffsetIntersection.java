package dev.nimbler.ide.model.text.difftextmodel;

import java.util.Objects;

import dev.nimbler.ide.model.text.TextEdit;
import dev.nimbler.ide.model.text.difftextmodel.ApplyTextEditResult.ProcessResult;

/**
 * Helper class for applying text edits to the diff text model.
 */
class DiffTextOffsetIntersection {

    /**
     * Algorithm for applying a {@link TextEdit} to current edits
     * 
     * @param textEdit the {@link TextEdit} to apply
     * @param offsetIntoSortedArray
     * @param priorEditPos offset into complete text of prior edit
     * @param priorEdit the prior edit
     * 
     * @return an {@link ApplyTextEditResult} containing add and remove {@link DiffTextChange} operations
     *         that have to be applied to the sorted array
     */
	static ApplyTextEditResult applyTextEdit(TextEdit textEdit, int offsetIntoSortedArray, long priorEditPos, DiffTextOffset priorEdit) {
		
	    Objects.requireNonNull(priorEdit);
	    
	    if (priorEditPos < 0L) {
	        throw new IllegalArgumentException();
	    }
	    
		final long offsetOfEditStart = textEdit.getStartPos();
		final long offsetofEditLastChar = textEdit.getStartPos() + textEdit.getNewLength() - 1;
		
		if (offsetofEditLastChar < offsetOfEditStart) {
			throw new IllegalStateException();
		}

		final Pos startPos 	= Pos.getPos(priorEditPos, priorEdit.getNewLength(), offsetOfEditStart);
		final Pos endPos 	= Pos.getPos(priorEditPos, priorEdit.getNewLength(), offsetofEditLastChar);

		return applyTextEdit(textEdit, offsetIntoSortedArray, priorEditPos, priorEdit, startPos, endPos);
	}

	private static ApplyTextEditResult applyTextEdit(
			TextEdit textEdit,
			int offsetIntoSortedArray,
			long priorEditPos,
			DiffTextOffset priorEdit,
			Pos startPos,
			Pos endPos) {
		
		final ApplyTextEditResult applyTextEditResult;
		
		switch (startPos) {
		case BEFORE:
			switch (endPos) {
			case BEFORE:
				applyTextEditResult = new ApplyTextEditResult(ProcessResult.COMPLETELY);

				final long distanceToNext = priorEditPos - (textEdit.getStartPos() + textEdit.getNewLength());
				
				applyTextEditResult.addAddedOffset(new DiffTextChange(textEdit, offsetIntoSortedArray, distanceToNext));
				break;
				
			case AT_START:
			case WITHIN:
			case AT_END:
				// Must split prior edit
				applyTextEditResult = merge(textEdit, offsetIntoSortedArray, priorEditPos, priorEdit, ProcessResult.COMPLETELY);
				break;
				
			case AFTER:
				applyTextEditResult = merge(textEdit, offsetIntoSortedArray, priorEditPos, priorEdit, ProcessResult.PARTLY);
				break;
				
			default:
				throw new UnsupportedOperationException();
			}
			break;
			
		case AT_START:
			switch (endPos) {
			case AT_START:
			case WITHIN:
			case AT_END:
				applyTextEditResult = merge(textEdit, offsetIntoSortedArray, priorEditPos, priorEdit, ProcessResult.COMPLETELY);
				break;
				
			case AFTER:
				applyTextEditResult = merge(textEdit, offsetIntoSortedArray, priorEditPos, priorEdit, ProcessResult.PARTLY);
				break;
				
			default:
				throw new UnsupportedOperationException();
			}
			break;
			
		case WITHIN:
			switch (endPos) {
			case WITHIN:
			case AT_END:
				applyTextEditResult = merge(textEdit, offsetIntoSortedArray, priorEditPos, priorEdit, ProcessResult.COMPLETELY);
				break;
			case AFTER:
				applyTextEditResult = merge(textEdit, offsetIntoSortedArray, priorEditPos, priorEdit, ProcessResult.PARTLY);
				break;
				
			default:
				throw new UnsupportedOperationException();
			}
			break;
			
		case AT_END:
			switch (endPos) {
			
			case AT_END:
				applyTextEditResult = merge(textEdit, offsetIntoSortedArray, priorEditPos, priorEdit, ProcessResult.COMPLETELY);
				break;

			case AFTER:
				applyTextEditResult = merge(textEdit, offsetIntoSortedArray, priorEditPos, priorEdit, ProcessResult.PARTLY);
				break;
				
			default:
				throw new UnsupportedOperationException();
			}
			break;
			
		case AFTER:
			// after this one
			applyTextEditResult = new ApplyTextEditResult(ProcessResult.NONE);
			break;
			
		default:
			throw new UnsupportedOperationException();
		}
		
		return applyTextEditResult;
	}
	
	private static ApplyTextEditResult merge(
			TextEdit textEdit,
			int offsetIdx,
			long priorEditPos,
			DiffTextOffset priorEdit,
			ProcessResult processResult) {

		final ApplyTextEditResult applyTextEditResult = new ApplyTextEditResult(processResult);
		
		final TextEdit merged = textEdit.merge(priorEdit.getTextEdit());
		
		if (merged == null) {
			throw new IllegalStateException();
		}
		
		final long distanceToNext = priorEditPos - (merged.getStartPos() + merged.getNewLength());

		applyTextEditResult.addAddedOffset(new DiffTextChange(merged, offsetIdx, distanceToNext));
		
		applyTextEditResult.addRemovedOffset(new DiffTextChange(priorEdit, offsetIdx));
		
		return applyTextEditResult;
	}
}
