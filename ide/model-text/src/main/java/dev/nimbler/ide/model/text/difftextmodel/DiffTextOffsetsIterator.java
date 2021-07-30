package dev.nimbler.ide.model.text.difftextmodel;

/**
 * An iterator for text diffs when operating on the diff text model
 * 
 * @param <T> type of state to be passed to iterators
 */
interface DiffTextOffsetsIterator<T> {

    /**
     * Called for each text difference to the initial model
     * 
     * @param offsetIntoWholeText offset of diff into the whole text
     * @param offset the diff and offset
     * @param state state passed to the iteration
     * 
     * @return whether to continue iteration
     */
    
	boolean onDiffTextOffset(long offsetIntoWholeText, DiffTextOffset offset, T state);
	
	/**
	 * Called if the model contains initial text. That is text before any diff, i.e. there is no diff at offset 0.
	 * E.g. if there is a diff at index 3, there are 3 characters of initial text before the first diff.
	 * 
	 * @param offsetIntoWholeText 0L
	 * @param offsetIntoInitial 0L
	 * @param lengthOfInitialText length of initial block of text before any diff
	 * @param state the state object passed to the iterator
	 * 
	 * @return whether to continue iteration
	 */
	boolean onInitialModelText(long offsetIntoWholeText, long offsetIntoInitial, long lengthOfInitialText, T state);

}
