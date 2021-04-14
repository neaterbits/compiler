package dev.nimbler.ide.model.text.difftextmodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;

public class SortedArrayTest {

	@Test
	public void testInsertAt() {
		
		final SortedArray<Integer> array = new SortedArray<>(Integer.class);
		
		array.insertAt(0, 1);
		array.insertAt(1, 2);
		array.insertAt(2, 4);
		
		assertThat(array.get(0)).isEqualTo(1);
		assertThat(array.get(1)).isEqualTo(2);
		assertThat(array.get(2)).isEqualTo(4);
		
		assertThat(array.length()).isEqualTo(3);
	}
	
	@Test
	public void testInsertAtContinousCheck() {

		final SortedArray<Integer> array = new SortedArray<>(Integer.class);

		try {
			array.insertAt(1, 3);
			
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
		
		assertThat(array.isEmpty()).isTrue();

		array.insertAt(0, 1);

		assertThat(array.get(0)).isEqualTo(1);
		
		assertThat(array.length()).isEqualTo(1);

		try {
			array.insertAt(2, 3);
			
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testInitialCapacity() {
		
		final int initialCapacity = 300;
		
		final SortedArray<Integer> array = new SortedArray<>(Integer.class, initialCapacity);

		final int size = initialCapacity * 3;
		
		for (int i = 0; i < size; ++ i) {
			final int value = i + 1234;

			array.insertAt(i, value);
			
			assertThat(array.get(i)).isEqualTo(value);
			assertThat(array.length()).isEqualTo(i + 1);
		}

		for (int i = 0; i < size; ++ i) {
			final int value = i + 1234;

			assertThat(array.get(i)).isEqualTo(value);
		}
	}
	
	@Test
	public void testInsertAtBeginning() {

		final SortedArray<Integer> array = new SortedArray<>(Integer.class);

		array.insertAt(0, 1);
		array.insertAt(1, 2);
		array.insertAt(2, 4);
		
		assertThat(array.get(0)).isEqualTo(1);
		assertThat(array.get(1)).isEqualTo(2);
		assertThat(array.get(2)).isEqualTo(4);
		
		array.insertAt(0, 3);

		assertThat(array.get(0)).isEqualTo(3);
		assertThat(array.get(1)).isEqualTo(1);
		assertThat(array.get(2)).isEqualTo(2);
		assertThat(array.get(3)).isEqualTo(4);
	
		assertThat(array.length()).isEqualTo(4);
	}
	
	@Test
	public void testInsertAtMiddle() {

		final SortedArray<Integer> array = new SortedArray<>(Integer.class);

		array.insertAt(0, 1);
		array.insertAt(1, 2);
		array.insertAt(2, 4);
		
		assertThat(array.get(0)).isEqualTo(1);
		assertThat(array.get(1)).isEqualTo(2);
		assertThat(array.get(2)).isEqualTo(4);
		
		array.insertAt(1, 3);

		assertThat(array.get(0)).isEqualTo(1);
		assertThat(array.get(1)).isEqualTo(3);
		assertThat(array.get(2)).isEqualTo(2);
		assertThat(array.get(3)).isEqualTo(4);
	
		assertThat(array.length()).isEqualTo(4);
	}

	@Test
	public void testRemoveMultiple() {

		final SortedArray<Integer> array = new SortedArray<>(Integer.class);

		array.insertAt(0, 123);
		array.insertAt(1, 234);
		array.insertAt(2, 456);
		array.insertAt(3, 567);
		array.insertAt(4, 678);
		array.insertAt(5, 789);
		
		array.removeMultiple(0, 2, 4);
		
		assertThat(array.length()).isEqualTo(3);
		assertThat(array.get(0)).isEqualTo(234);
		assertThat(array.get(1)).isEqualTo(567);
		assertThat(array.get(2)).isEqualTo(789);
		
		array.reAddMultiple(
				Arrays.asList(123, 456, 678),
				Function.identity(),
				value -> {
					
					final int index;
					switch (value) {
					case 123: index = 0; break;
					case 456: index = 2; break;
					case 678: index = 4; break;
					
					default:
						throw new UnsupportedOperationException();
					}

					return index;
				});
		
		assertThat(array.length()).isEqualTo(6);
		
		assertThat(array.get(0)).isEqualTo(123);
		assertThat(array.get(1)).isEqualTo(234);
		assertThat(array.get(2)).isEqualTo(456);
		assertThat(array.get(3)).isEqualTo(567);
		assertThat(array.get(4)).isEqualTo(678);
		assertThat(array.get(5)).isEqualTo(789);
	}

	@Test
	public void testRemoveMultipleBegin() {

		final SortedArray<Integer> array = new SortedArray<>(Integer.class);

		array.insertAt(0, 123);
		array.insertAt(1, 234);
		array.insertAt(2, 456);
		array.insertAt(3, 567);
		array.insertAt(4, 678);
		array.insertAt(5, 789);
		
		array.removeMultiple(0, 1, 2);
		
		assertThat(array.length()).isEqualTo(3);
		assertThat(array.get(0)).isEqualTo(567);
		assertThat(array.get(1)).isEqualTo(678);
		assertThat(array.get(2)).isEqualTo(789);
		
		array.reAddMultiple(
				Arrays.asList(123, 234, 456),
				Function.identity(),
				value -> {
					
					final int index;
					switch (value) {
					case 123: index = 0; break;
					case 234: index = 1; break;
					case 456: index = 2; break;
					
					default:
						throw new UnsupportedOperationException();
					}

					return index;
				});
		
		assertThat(array.length()).isEqualTo(6);
		
		assertThat(array.get(0)).isEqualTo(123);
		assertThat(array.get(1)).isEqualTo(234);
		assertThat(array.get(2)).isEqualTo(456);
		assertThat(array.get(3)).isEqualTo(567);
		assertThat(array.get(4)).isEqualTo(678);
		assertThat(array.get(5)).isEqualTo(789);
	}

	@Test
	public void testRemoveMultipleLast() {

		final SortedArray<Integer> array = new SortedArray<>(Integer.class);

		array.insertAt(0, 123);
		array.insertAt(1, 234);
		array.insertAt(2, 456);
		array.insertAt(3, 567);
		array.insertAt(4, 678);
		array.insertAt(5, 789);
		
		array.removeMultiple(1, 3, 5);
		
		assertThat(array.length()).isEqualTo(3);
		assertThat(array.get(0)).isEqualTo(123);
		assertThat(array.get(1)).isEqualTo(456);
		assertThat(array.get(2)).isEqualTo(678);

		array.reAddMultiple(
				Arrays.asList(234, 567, 789),
				Function.identity(),
				value -> {
					
					final int index;
					switch (value) {
					case 234: index = 1; break;
					case 567: index = 3; break;
					case 789: index = 5; break;
					
					default:
						throw new UnsupportedOperationException();
					}

					return index;
				});
		
		assertThat(array.length()).isEqualTo(6);
		
		assertThat(array.get(0)).isEqualTo(123);
		assertThat(array.get(1)).isEqualTo(234);
		assertThat(array.get(2)).isEqualTo(456);
		assertThat(array.get(3)).isEqualTo(567);
		assertThat(array.get(4)).isEqualTo(678);
		assertThat(array.get(5)).isEqualTo(789);
}
}
