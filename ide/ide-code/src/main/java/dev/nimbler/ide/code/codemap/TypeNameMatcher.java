package dev.nimbler.ide.code.codemap;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.language.common.types.TypeName;

@FunctionalInterface
interface TypeNameMatcher {

	TypeName matches(TypeName typeNameIfKnown, SourceFileResourcePath sourceFileResourcePath, String namespace, String name);
	
}
