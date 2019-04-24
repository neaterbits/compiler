package com.neaterbits.compiler.resolver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Strings;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;

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
	
	protected static CompiledType<ComplexType<?, ?, ?>> makeCompiledType(
			CompiledFile<ComplexType<?, ?, ?>, CompilationUnit>
			compiledFile,
			String name,
			TypeVariant typeVariant, 
			ScopedName ... extendsFrom) {
		
		final ScopedName scopedName = makeScopedName(name);

		final CompiledType<ComplexType<?, ?, ?>> compiledType = new TestCompiledType(
				compiledFile.getSpec(),
				new TypeSpec(scopedName, typeVariant),
				null,
				null,
				Arrays.stream(extendsFrom)
					.map(extendsFromName -> new CompiledTypeDependency(extendsFromName, ReferenceType.EXTENDS_FROM, -1, null, null))
					.collect(Collectors.toList()),
				null);

		return compiledType;
	}
	
	@SafeVarargs
	protected static ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> makeResolvedType(
			ASTTypesModel<CompilationUnit, BuiltinType, ComplexType<?, ?, ?>, TypeName> astModel,
			ResolvedFile<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolvedFile,
			String name,
			TypeVariant typeVariant,
			ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> ... extendsFrom) {
		
		final ScopedName scopedName = makeScopedName(name);
		
		final List<ResolvedTypeDependency> extendsFromDependencies = Arrays.stream(extendsFrom)
				.map(type -> new ResolvedTypeDependency(
						type.getTypeName(),
						ReferenceType.EXTENDS_FROM,
						-1, TypeResolveMode.CLASSNAME_TO_COMPLETE,
						type.getSpec().getTypeVariant(),
						null, null))
				.collect(Collectors.toList());
		
		final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolvedType
				= new TestResolvedType(resolvedFile.getSpec(), scopedName, typeVariant, null, null, extendsFromDependencies, null);

		return resolvedType;
	}
}
