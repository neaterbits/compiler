package dev.nimbler.compiler.language.java;

import java.util.Arrays;

import com.neaterbits.util.Strings;

import dev.nimbler.language.common.types.TypeName;

public class JavaUtil {

	public static TypeName parseToTypeName(String type) {
		final String [] parts = Strings.split(type, '.');
		
		final TypeName typeName = new TypeName(
				parts.length > 1 ? Arrays.copyOf(parts, parts.length - 1) : null,
				null,
				parts[parts.length - 1]);
		
		return typeName;
	}
}
