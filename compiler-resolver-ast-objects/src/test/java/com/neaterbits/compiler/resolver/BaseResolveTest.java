package com.neaterbits.compiler.resolver;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.model.common.util.ReferenceLocation;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeName;

public abstract class BaseResolveTest {

	protected static ScopedName makeScopedName(String name) {
		final String [] parts = Strings.split(name, '.');

		final ScopedName scopedName = ScopedName.makeScopedName(parts, parts.length - 1, parts[parts.length - 1]);

		return scopedName;
	}

	protected static TypeName makeTypeName(String name) {
		final String [] parts = Strings.split(name, '.');

		final TypeName typeName = new TypeName(
				Arrays.copyOf(parts, parts.length - 1),
				null,
				parts[parts.length - 1]);

		return typeName;
	}

	protected static CompiledType makeCompiledType(
			CompiledFile<CompilationUnit>
			compiledFile,
			String name,
			TypeVariant typeVariant,
			ScopedName ... extendsFrom) {

		final ScopedName scopedName = makeScopedName(name);

		final CompiledType compiledType = new CompiledType(
				compiledFile.getSpec(),
				new TypeSpec(scopedName, typeVariant),
				null,
				null,
				Arrays.stream(extendsFrom)
					.map(extendsFromName -> new CompiledTypeDependency(extendsFromName, ReferenceLocation.EXTENDS_FROM, -1, null, null))
					.collect(Collectors.toList()),
				null);

		return compiledType;
	}
}
