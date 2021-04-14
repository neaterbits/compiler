package dev.nimbler.ide.core.model.codemap;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.language.common.types.TypeName;

@FunctionalInterface
interface TypeNameMatcher {

	TypeName matches(TypeName typeNameIfKnown, SourceFileResourcePath sourceFileResourcePath, String namespace, String name);
	
}
