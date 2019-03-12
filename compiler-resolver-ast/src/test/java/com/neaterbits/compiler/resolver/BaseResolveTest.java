package com.neaterbits.compiler.resolver;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.references.TestResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
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
					.map(extendsFromName -> new TestCompiledTypeDependency(extendsFromName, TypeVariant.CLASS, ReferenceType.EXTENDS_FROM))
					.collect(Collectors.toList()),
				null);

		return compiledType;
	}
	
	@SafeVarargs
	protected static ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> makeResolvedType(
			ASTTypesModel<BuiltinType, ComplexType<?, ?, ?>, TypeName> astModel,
			ResolvedFile<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolvedFile,
			String name,
			TypeVariant typeVariant,
			ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> ... extendsFrom) {
		
		final ScopedName scopedName = makeScopedName(name);
		
		final List<ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName>> extendsFromDependencies = Arrays.stream(extendsFrom)
				.map(type -> new TestResolvedTypeDependency(
						type.getTypeName(),
						ReferenceType.EXTENDS_FROM,
						type.getSpec().getTypeVariant()))
				.collect(Collectors.toList());
		
		final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolvedType
				= new TestResolvedType(resolvedFile.getSpec(), scopedName, typeVariant, null, null, extendsFromDependencies, null);

		return resolvedType;
	}
}
