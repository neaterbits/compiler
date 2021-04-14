package dev.nimbler.ide.model.text.difftextmodel;

import java.util.Objects;

import dev.nimbler.ide.model.text.TextEdit;

final class DiffTextChange {

	private final DiffTextOffset diffTextOffset;
	private final int offsetIntoSortedArray;

	DiffTextChange(TextEdit textEdit, int offsetIntoSortedArray, long distanceToNext) {
		this(new DiffTextOffset(textEdit, distanceToNext), offsetIntoSortedArray);
	}

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
