package dev.nimbler.ide.model.text.difftextmodel;

/**
 * Base class for diff text iterators that count various, e.g. number of lines.
 * 
 * @param <T> type of iterator state, must subclass {@link CounterState}
 */
abstract class CountingIterator<T extends CountingIterator.CounterState> implements DiffTextOffsetsIterator<T> {

    /**
     * Base class for state object for {@link CountingIterator}
     */
	static class CounterState {
		
		private long val;
		
		final long getCounter() {
			return val;
		}
		
		final void addToCounter(long val) {
			this.val += val;
		}
	}
}
