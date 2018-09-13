package com.neaterbits.compiler.common.util;

import java.util.ArrayList;
import java.util.List;

public class Strings {

	private static final String EMPTY = "";
	
	public static String[] split(String s, char c) {
		final int length = s.length();

		final List<String> strings = new ArrayList<String>(100);

		int last = -1;

		for (int i = 0; i < length; ++i) {
			if (s.charAt(i) == c) {

				final String found;

				if (i == 0) {
					found = EMPTY;
				} else if (i - last == 1) {
					found = EMPTY;
				} else {
					found = s.substring(last + 1, i);
				}

				last = i;

				if (found == null) {
					throw new IllegalStateException("should have found entry");
				}
				strings.add(found);
			}
		}

		strings.add(s.substring(last + 1, length));

		return strings.toArray(new String[strings.size()]);
	}

	public static String join(String[] strings, char separator) {
		final StringBuilder sb = new StringBuilder();

		for (int i = 0; i < strings.length; ++i) {
			if (i > 0) {
				sb.append(separator);
			}

			sb.append(strings[i]);
		}

		return sb.toString();
	}
}
