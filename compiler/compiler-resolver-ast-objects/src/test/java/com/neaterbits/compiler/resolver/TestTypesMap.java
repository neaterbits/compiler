package com.neaterbits.compiler.resolver;

import java.util.HashMap;
import java.util.Map;

import com.neaterbits.language.common.types.ScopedName;
import com.neaterbits.language.common.types.TypesMap;
import com.neaterbits.util.Strings;

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
