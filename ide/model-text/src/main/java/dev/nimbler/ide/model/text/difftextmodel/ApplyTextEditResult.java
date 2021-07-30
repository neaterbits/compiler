package dev.nimbler.ide.model.text.difftextmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.ide.model.text.TextEdit;

/**
 * Class for the result of applying a {@link TextEdit} to the diff text model
 */
final class ApplyTextEditResult {

    /**
     * Processing result enum
     */
	enum ProcessResult {
	    
	    /**
	     * {@link TextEdit} was completely applied
	     */
		COMPLETELY,
		
		/**
		 * {@link TextEdit} was partly applied
		 */
		PARTLY,
		
		/**
		 * {@link TextEdit} was not applied at all
		 */
		NONE;
	}
	
	private final ProcessResult processResult;
	
	/**
	 * List of {@link DiffTextOffset} objects that should be removed
	 */
	private List<DiffTextChange> removedOffsets;
	
	/**
	 * List of {@link DiffTextOffset} objects that should be added
	 */
	private List<DiffTextChange> addedOffsets;

	ApplyTextEditResult(ProcessResult processResult) {

		Objects.requireNonNull(processResult);
		
		this.processResult = processResult;
	}

	/**
	 * Get the {@link ProcessResult} of how was applied
	 * 
	 * @return the {@link ProcessResult}
	 */
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

	/**
	 * Apply the computed result to an array of {@link DiffTextOffset} objects
	 * 
	 * @param array the array of {@link DiffTextOffset} objects
	 */
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
