package dev.nimbler.compiler.resolver;

import java.util.HashMap;
import java.util.Map;

import org.jutils.Strings;

import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypesMap;

final class TestTypesMap implements TypesMap<String> {

	private final Map<ScopedName, String> map;
	
	TestTypesMap(String ... strings) {

		this.map = new HashMap<>();
		
		for (String string : strings) {
		
			final String [] parts = Strings.split(string, '.');
			
			map.put(ScopedName.makeScopedName(parts), string);
		}
	}

	@Override
	public String lookupByScopedName(ScopedName scopedName) {
		return map.get(scopedName);
	}

	@Override
	public String toString() {
		return map.toString();
	}
}
