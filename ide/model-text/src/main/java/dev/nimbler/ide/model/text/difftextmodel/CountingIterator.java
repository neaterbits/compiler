package dev.nimbler.ide.model.text.difftextmodel;

abstract class CountingIterator<T extends CountingIterator.CounterState> implements DiffTextOffsetsIterator<T> {

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
