package com.neaterbits.ide.core.model.codemap;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.build.types.resource.SourceFileResourcePath;

@FunctionalInterface
interface TypeNameMatcher {

	TypeName matches(TypeName typeNameIfKnown, SourceFileResourcePath sourceFileResourcePath, String namespace, String name);
	
}
