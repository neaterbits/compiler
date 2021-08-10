package dev.nimbler.ide.model.text.difftextmodel;

import java.util.Objects;

import dev.nimbler.ide.model.text.PosEdit;
import dev.nimbler.ide.model.text.TextEdit;

/**
 * Holds a change in the diff text model, i.e. for adding or removing {@link DiffTextOffset} objects
 * to the current model.
 * 
 */
final class DiffTextChange {

    /**
     * The {@link DiffTextOffset} to add or remove
     */
	private final DiffTextOffset diffTextOffset;
	
	/**
	 * Offset into current sorted array of {@link DiffTextOffset} at where to add or remove
	 */
	private final int offsetIntoSortedArray;

	/**
	 * Construct a {@link DiffTextChange} from a new {@link TextEdit}
	 * 
	 * @param edit the {@link PosEdit} for the diff
	 * @param offsetIntoSortedArray index to insert at into 
	 * @param distanceToNext number of characters to start of next {@link DiffTextModel} in sorted array of {@link DiffTextOffset}
	 */
	DiffTextChange(PosEdit edit, int offsetIntoSortedArray, long distanceToNext) {
		this(new DiffTextOffset(edit, distanceToNext), offsetIntoSortedArray);
	}

	/**
	 * Construct a new {@link DiffTextChange} for removal of {@link DiffTextOffset} in sorted array of {@link DiffTextModel}
	 * 
	 * @param diffTextOffset the {@link DiffTextOffset} to remove
	 * @param offsetIntoSortedArray the offset of {@link DiffTextOffset} into sorted array
	 */
	DiffTextChange(DiffTextOffset diffTextOffset, int offsetIntoSortedArray) {

		Objects.requireNonNull(diffTextOffset);
		
		this.diffTextOffset = diffTextOffset;
		this.offsetIntoSortedArray = offsetIntoSortedArray;
	}

	DiffTextOffset getDiffTextOffset() {
		return diffTextOffset;
	}

	int getOffsetIntoSortedArray() {
		return offsetIntoSortedArray;
	}
}
