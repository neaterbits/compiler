package com.neaterbits.ide.core.model.codemap;

import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.language.common.types.TypeName;

@FunctionalInterface
interface TypeNameMatcher {

	TypeName matches(TypeName typeNameIfKnown, SourceFileResourcePath sourceFileResourcePath, String namespace, String name);
	
}
