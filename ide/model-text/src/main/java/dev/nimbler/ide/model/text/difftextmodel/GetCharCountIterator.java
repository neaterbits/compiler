package dev.nimbler.ide.model.text.difftextmodel;

/**
 * Iterator for computing char count from diff text model
 */
final class GetCharCountIterator implements DiffTextOffsetsIterator<LineCountingIterator.CounterState> {

	@Override
	public boolean onDiffTextOffset(long offsetIntoWholeText, DiffTextOffset offset, LineCountingIterator.CounterState state) {
		state.addToCounter(offset.getNewLength());
		
		return true;
	}

	@Override
	public boolean onInitialModelText(long offsetIntoWholeText, long offsetIntoInitial, long lengthOfInitialText, LineCountingIterator.CounterState state) {
		state.addToCounter(lengthOfInitialText);
		
		return true;
	}
}
