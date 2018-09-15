package com.neaterbits.compiler.common.util;

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
}
