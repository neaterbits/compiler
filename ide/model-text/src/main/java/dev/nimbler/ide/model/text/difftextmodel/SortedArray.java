package dev.nimbler.ide.model.text.difftextmodel;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.jutils.ArrayUtils;

/**
 * Generic array wrapper class for dynamically resized array of objects
 * 
 * @param <T> type of objects stored in array
 * 
 */
final class SortedArray<T> implements UnmodifiableSortedArray<T> {

    // Allocate 3 times asked for size
    private static final int ARRAY_EXPAND_FACTOR = 3;
    
	private final Class<T> componentType;
	private final int initialCapacity;
	
	private T [] array;
	private int num;

	SortedArray(Class<T> componentType) {
		this(componentType, 10000);
	}

	SortedArray(Class<T> componentType, int initialCapacity) {
	
		Objects.requireNonNull(componentType);

        if (initialCapacity <= 0) {
            throw new IllegalArgumentException();
        }

		this.componentType = componentType;
		this.initialCapacity = initialCapacity;
	}

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
			
			this.array = allocArray(initialCapacity);
		}
		else if (index >= array.length) {
		    updatedNum = index + 1;

			this.array = Arrays.copyOf(array, index * ARRAY_EXPAND_FACTOR);
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

	<U> void insertMultiple(List<U> toInsert, Function<U, T> getObj, Function<U, Integer> getIndex) {

		final int numToInsert = toInsert.size();
		
		if (array == null) {
			this.array = allocArray(Math.max(numToInsert, initialCapacity));
		}
		else if (num + numToInsert > array.length) {

			this.array = Arrays.copyOf(array, (num + numToInsert) * ARRAY_EXPAND_FACTOR);
		}

		reAddMultiple(toInsert, getObj, getIndex);
	}

	public void set(int index, T value) {
        
	    Objects.requireNonNull(value);
	    
        if (index >= num) {
            throw new IllegalArgumentException();
        }
        
        array[index] = value;
    }

	@Override
	public T get(int index) {
		
		if (index >= num) {
			throw new IllegalArgumentException();
		}
		
		return array[index];
	}

	private void move(int startIndex, int delta, int count) {

	    ArrayUtils.move(array, startIndex, delta, count);
	}

    /**
     * Replace all objects at specified indices, add specified and compress array.
     * 
     * @param indices the indices to remove.
     */
    void replace(int atIndex, int count, Collection<T> toAdd) {
        
        Objects.requireNonNull(toAdd);
        
        if (atIndex < 0) {
            throw new IllegalArgumentException();
        }
        
        if (count < 0) {
            throw new IllegalArgumentException();
        }

        if (count == 0) {
            // No reason to replace with count 0 to replaced, might as well add
            throw new IllegalArgumentException();
        }
        
        if (atIndex + count > num) {
            throw new IllegalArgumentException();
        }
        
        if (toAdd.isEmpty()) {
            // No reason to replace with none to add
            throw new IllegalArgumentException();
        }

        final int updatedNum = num - count + toAdd.size();
        
        if (array.length < updatedNum) {

            // Must expand array
            final T[] updateArray = allocArray(updatedNum * ARRAY_EXPAND_FACTOR);
            
            // Can now copy entries since must move all anyways
            // No need to remove any objects from existing array
            if (atIndex > 0) {
                System.arraycopy(
                        array, 0,
                        updateArray, 0, atIndex);
            }
            
            // Set objects to be added
            ArrayUtils.setAt(updateArray, atIndex, toAdd);

            // Copy any objects after the replace operation
            final int numSrcEntriesAfterRemoveIndex = num - count - atIndex;

            System.arraycopy(
                    array,
                    atIndex + count,
                    
                    updateArray,
                    atIndex + toAdd.size(),
                    numSrcEntriesAfterRemoveIndex);
            
            this.array = updateArray;
        }
        else {
            // Reuse same array so move entries around
            
            if (count != toAdd.size()) {

                final int moveIndex = atIndex + count;
                final int toMove = num - moveIndex;
                
                if (toMove < 0) {
                    throw new IllegalStateException();
                }
                else if (toMove == 0) {
                    // Nothing to do
                }
                else if (toMove > 0) {

                    // Must move some entries around
                    // to to add > count, then delta should be positive
                    final int delta = toAdd.size() - count;
                    
                    move(moveIndex, delta, toMove);
                }
            }
            
            // update entries after moved
            ArrayUtils.setAt(array, atIndex, toAdd);
        }

        this.num = updatedNum;
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
			    
			    final int toMove = index - lastIndex - 1;

                if (toMove < 0) {
                    throw new IllegalStateException();
                }
                else if (toMove == 0) {
                    // Nothing to do
                }
                else if (toMove > 0) {
			        move(lastIndex + 1, - delta, toMove);
			    }
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
		
        final int toMove = num + toAdd.size() - lastIndex - 1;
        
        if (toMove < 0) {
            throw new IllegalStateException();
        }
        else if (toMove == 0) {
            // Nothing to do
        }
        else if (toMove > 0) {
            move(lastIndex - delta + 1, delta, toMove);
        }
		
		-- delta;
		
		for (int i = toAdd.size() - 1; i >= 0; -- i) {

			final U obj = toAdd.get(i);
			
			final int index = getIndex.apply(obj);

			if (i > 0) {
				final int prevIndex = getIndex.apply(toAdd.get(i - 1));
				
				final int diff = index - prevIndex - 1;
				
				if (diff < 0) {
				    throw new IllegalStateException();
				}
				else if (diff == 0) {
				    // Nothing to do
				}
				else {
				    move(prevIndex - delta + 1, delta, diff);
				}
			}
			
			array[index] = getObj.apply(obj);
			
			-- delta;
		}
		
		num += toAdd.size();
	}

	@SuppressWarnings("unchecked")
    private T[] allocArray(int capacity) {
	 
	    return (T[])Array.newInstance(componentType, capacity);
	}
	
	@Override
	public int length() {
		return num;
	}
	
	@Override
	public boolean isEmpty() {
		return num == 0;
	}

	@Override
	public String toString() {
		return array != null ? Arrays.toString(Arrays.copyOf(array, num)) : "[]";
	}
}
