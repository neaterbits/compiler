package com.neaterbits.compiler.common.resolver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.TypeSpec;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.util.Strings;

public abstract class BaseResolveTest {

	protected static ScopedName makeScopedName(String name) {
		final String [] parts = Strings.split(name, '.');
		
		final ScopedName scopedName = ScopedName.makeScopedName(parts, parts.length - 1, parts[parts.length - 1]);

		return scopedName;
	}

	protected static CompiledType makeCompiledType(CompiledFile compiledFile, String name, TypeVariant typeVariant, ScopedName ... extendsFrom) {
		final ScopedName scopedName = makeScopedName(name);

		final CompiledType compiledType = new TestCompiledType(
				compiledFile.getSpec(),
				new TypeSpec(scopedName, typeVariant),
				null,
				Arrays.asList(extendsFrom), null);

		return compiledType;
	}
	
	protected static ResolvedType makeResolvedType(ResolvedFile resolvedFile, String name, TypeVariant typeVariant, ResolvedType ... extendsFrom) {
		final ScopedName scopedName = makeScopedName(name);
		
		final List<ResolvedTypeDependency> extendsFromDependencies = Arrays.stream(extendsFrom)
				.map(type -> new TestDependency(type, ReferenceType.EXTENDS_FROM))
				.collect(Collectors.toList());
		
		final ResolvedType resolvedType = new TestResolvedType(resolvedFile.getSpec(), scopedName, typeVariant, null, extendsFromDependencies, null);

		return resolvedType;
	}
}
