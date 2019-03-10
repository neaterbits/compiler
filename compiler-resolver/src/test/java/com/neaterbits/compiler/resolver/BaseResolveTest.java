package com.neaterbits.compiler.resolver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.loader.CompiledFile;
import com.neaterbits.compiler.resolver.loader.CompiledType;
import com.neaterbits.compiler.resolver.loader.ResolvedFile;
import com.neaterbits.compiler.resolver.loader.ResolvedType;
import com.neaterbits.compiler.resolver.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.loader.TypeSpec;
import com.neaterbits.compiler.resolver.references.TestResolvedTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Strings;

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
				null,
				Arrays.stream(extendsFrom)
					.map(extendsFromName -> new TestCompiledTypeDependency(extendsFromName, TypeVariant.CLASS, ReferenceType.EXTENDS_FROM))
					.collect(Collectors.toList()),
				null);

		return compiledType;
	}
	
	protected static ResolvedType makeResolvedType(ResolvedFile resolvedFile, String name, TypeVariant typeVariant, ResolvedType ... extendsFrom) {
		final ScopedName scopedName = makeScopedName(name);
		
		final List<ResolvedTypeDependency> extendsFromDependencies = Arrays.stream(extendsFrom)
				.map(type -> new TestResolvedTypeDependency(type.getCompleteName(), ReferenceType.EXTENDS_FROM, type.getSpec().getTypeVariant()))
				.collect(Collectors.toList());
		
		final ResolvedType resolvedType = new TestResolvedType(resolvedFile.getSpec(), scopedName, typeVariant, null, null, extendsFromDependencies, null);

		return resolvedType;
	}
}
