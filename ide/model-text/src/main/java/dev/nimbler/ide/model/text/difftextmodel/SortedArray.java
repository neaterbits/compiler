package dev.nimbler.ide.model.text.difftextmodel;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * Generic array wrapper class for dynamically resized array of objects
 * 
 * @param <T> type of objects stored in array
 */
final class SortedArray<T> {

	private final Class<T> componentType;
	
	private final int initialCapacity;
	
	private T [] array;
	private int num;

	SortedArray(Class<T> componentType) {
		this(componentType, 10000);
	}

	SortedArray(Class<T> componentType, int initialCapacity) {
	
		Objects.requireNonNull(componentType);
		
		this.componentType = componentType;
		this.initialCapacity = initialCapacity;
	}

	
	@SuppressWarnings("unchecked")
	void insertAt(int index, T object) {

		Objects.requireNonNull(object);
		
		if (index < 0) {
			throw new IllegalArgumentException();
		}
		
		if (index > num) {
			throw new IllegalArgumentException();
		}
		
		Objects.requireNonNull(object);
		
		final int updatedNum;
		
		if (array == null) {
			
			updatedNum = index + 1;
			
			this.array = (T[])Array.newInstance(componentType, initialCapacity);
		}
		else if (index >= array.length) {

			this.array = Arrays.copyOf(array, index * 3);
			
			updatedNum = index + 1;
		}
		else if (num > array.length) {
			throw new IllegalStateException();
		}
		else if (index >= num) {
			updatedNum = index + 1;
		}
		else if (num == array.length) {
			updatedNum = num + 1;
		}
		else {
			updatedNum = num + 1;
		}
			
		if (index < num) {
			System.arraycopy(array, index, array, index + 1, num - index);
		}
			
		num = updatedNum;

		array[index] = object;
	}

	@SuppressWarnings("unchecked")
	<U> void insertMultiple(List<U> toInsert, Function<U, T> getObj, Function<U, Integer> getIndex) {

		final int numToInsert = toInsert.size();
		
		if (array == null) {
			this.array = (T[])Array.newInstance(componentType, Math.max(numToInsert, initialCapacity));
		}
		else if (num + numToInsert > array.length) {

			this.array = Arrays.copyOf(array, (num + numToInsert) * 3);
		}

		reAddMultiple(toInsert, getObj, getIndex);
	}

	T get(int index) {
		
		if (index >= num) {
			throw new IllegalArgumentException();
		}
		
		return array[index];
	}

	private void move(int startIndex, int delta, int count) {
		
		for (int i = 0; i < count; ++ i) {
			
			final int from = startIndex + i;
			final int to = startIndex + i + delta;
			System.out.println("## move from " + from + " to " + to + " delta " + delta);
			
			 //+ " lastIndex " + lastIndex + " index " + index);

			array[to] = array[from];
		}
	}
	
	/**
	 * Remove all objects at specified indices and compress array.
	 * 
	 * @param indices the indices to remove.
	 */
	void removeMultiple(int ... indices) {

		int delta = 0;
		int lastIndex = -1;
		
		for (int index : indices) {
			
			if (index <= lastIndex) {
				throw new IllegalStateException();
			}

			if (lastIndex != -1) {
				move(lastIndex + 1, - delta, index - lastIndex - 1);
			}
			
			++ delta;
			lastIndex = index;
		}

		if (lastIndex != num - 1) {
			move(lastIndex + 1, - delta, num - lastIndex - 1);
		}
		
		num -= indices.length;
		
		clear(num, indices.length);
	}
	
	private void clear(int idx, int count) {
		
		for (int i = 0; i < count; ++ i) {
			array[i + idx] = null;
		}
	}

	<U> void removeMultiple(Collection<U> toRemove, Function<U, T> getObj, Function<U, Integer> getIndex) {
		
		Objects.requireNonNull(toRemove);
		
		if (toRemove.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		for (U obj : toRemove) {
			final int index = getIndex.apply(obj);

			if (getObj.apply(obj) != array[index]) {
				throw new IllegalArgumentException();
			}
		}

		removeMultiple(toRemove.stream()
				.map(getIndex)
				.mapToInt(Integer::intValue)
				.toArray());
	}

	<U> void reAddMultiple(List<U> toAdd, Function<U, T> getObj, Function<U, Integer> getIndex) {
		
		Objects.requireNonNull(toAdd);
		
		if (toAdd.isEmpty()) {
			throw new IllegalArgumentException();
		}

		if (num + toAdd.size() > array.length) {
			throw new IllegalStateException();
		}
		
		int delta = toAdd.size();
		
		final U lastObj = toAdd.get(toAdd.size() - 1);
		
		final int lastIndex = getIndex.apply(lastObj);
		
		move(lastIndex - delta + 1, delta, num + toAdd.size() - lastIndex - 1);
		
		-- delta;
		
		for (int i = toAdd.size() - 1; i >= 0; -- i) {

			final U obj = toAdd.get(i);
			
			final int index = getIndex.apply(obj);

			if (i > 0) {
				final int prevIndex = getIndex.apply(toAdd.get(i - 1));
				
				final int diff = index - prevIndex - 1;
				
				System.out.println("## move from prevIndex " + prevIndex + " to " + index);
				
				move(prevIndex - delta + 1, delta, diff);
			}
			
			array[index] = getObj.apply(obj);
			
			-- delta;
		}
		
		num += toAdd.size();
	}
	
	int length() {
		return num;
	}
	
	boolean isEmpty() {
		return num == 0;
	}

	@Override
	public String toString() {
		return array != null ? Arrays.toString(Arrays.copyOf(array, num)) : "[]";
	}
}
