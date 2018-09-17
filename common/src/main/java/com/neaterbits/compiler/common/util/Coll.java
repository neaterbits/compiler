package com.neaterbits.compiler.common.util;

import java.util.Collection;
import java.util.function.Predicate;

import org.assertj.core.util.Objects;

public class Coll {

	public static <T> boolean has(T [] array, T item) {
		for (int i = 0; i < array.length; ++ i) {
			if (Objects.areEqual(array[i], item)) {
				return true;
			}
		}
		
		return false;
	}

	public static <T> T find(Collection<T> coll, Predicate<T> match) {
		
		for (T item : coll) {
			if (match.test(item)) {
				return item;
			}
		}
		
		return null;
	}

	public static <S, T> T find(Collection<S> coll, java.util.function.Function<S, T> map, Predicate<T> match) {
		
		for (S item : coll) {
			
			final T t = map.apply(item);
			
			if (match.test(t)) {
				return t;
			}
		}
		
		return null;
	}

}
