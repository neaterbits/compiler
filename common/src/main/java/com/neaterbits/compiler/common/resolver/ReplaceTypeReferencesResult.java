package com.neaterbits.compiler.common.resolver;

import java.util.Objects;

import com.neaterbits.compiler.common.resolver.codemap.ResolvedTypeCodeMapImpl;

public final class ReplaceTypeReferencesResult {

	private final ResolvedTypeCodeMapImpl codeMap;

	public ReplaceTypeReferencesResult(ResolvedTypeCodeMapImpl codeMap) {

		Objects.requireNonNull(codeMap);
		
		this.codeMap = codeMap;
	}
	
	public CodeMap getCodeMap() {
		return codeMap;
	}
}
