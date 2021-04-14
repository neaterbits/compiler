package dev.nimbler.ide.model.text.difftextmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class ApplyTextEditResult {

	enum ProcessResult {
		COMPLETELY,
		PARTLY,
		NONE;
	}
	
	private final ProcessResult processResult;
	private List<DiffTextChange> removedOffsets;
	private List<DiffTextChange> addedOffsets;

	ApplyTextEditResult(ProcessResult processResult) {

		Objects.requireNonNull(processResult);
		
		this.processResult = processResult;
	}

	ProcessResult getProcessResult() {
		return processResult;
	}

	private List<DiffTextChange> checkAdd(List<DiffTextChange> changeList, DiffTextChange change) {
		
		Objects.requireNonNull(change);
		
		final List<DiffTextChange> list;
		
		if (changeList != null) {
			
			if (changeList.contains(change)) {
				throw new IllegalStateException();
			}
			
			list = changeList;
		}
		else {
			list = new ArrayList<>();
		}

		list.add(change);
		
		return list;
	}
	
	final void addRemovedOffset(DiffTextChange offset) {
		this.removedOffsets = checkAdd(removedOffsets, offset);
	}
	
	final void addAddedOffset(DiffTextChange offset) {
		this.addedOffsets = checkAdd(addedOffsets, offset);
	}

	final void applyToArray(SortedArray<DiffTextOffset> array) {
		
		Objects.requireNonNull(array);
		
		if (removedOffsets != null && !removedOffsets.isEmpty()) {
			array.removeMultiple(removedOffsets, DiffTextChange::getDiffTextOffset, DiffTextChange::getOffsetIntoSortedArray);
		}

		if (addedOffsets != null && !addedOffsets.isEmpty()) {
			array.insertMultiple(addedOffsets, DiffTextChange::getDiffTextOffset, DiffTextChange::getOffsetIntoSortedArray);
		}
	}
	
	final void unApplyToArray(SortedArray<DiffTextOffset> array) {
		
		if (addedOffsets != null && !addedOffsets.isEmpty()) {
			array.removeMultiple(addedOffsets, DiffTextChange::getDiffTextOffset, DiffTextChange::getOffsetIntoSortedArray);
		}
		
		if (removedOffsets != null && !removedOffsets.isEmpty()) {
			array.reAddMultiple(removedOffsets, DiffTextChange::getDiffTextOffset, DiffTextChange::getOffsetIntoSortedArray);
		}
	}
	
}
