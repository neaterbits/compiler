package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.resolver.types.BaseResolverType;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.codemap.TypeVariant;

public class TestResolvedType extends BaseResolverType implements ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> {
	
	private final ComplexType<?, ?, ?> type;
	
	private final List<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> nestedTypes;
	private final List<ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName>> extendsFrom;
	private final List<ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName>> dependencies;
	
	public TestResolvedType(
			FileSpec file,
			ScopedName scopedName,
			TypeVariant typeVariant,
			ComplexType<?, ?, ?> type,
			List<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> nestedTypes,
			List<ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName>> extendsFrom,
			List<ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName>> dependencies) {

		super(file, new TypeSpec(scopedName, typeVariant));

		this.type = type;
		
		this.nestedTypes = nestedTypes;
		this.extendsFrom = extendsFrom;
		this.dependencies = dependencies;
	}

	public TestResolvedType(
			FileSpec file,
			ScopedName scopedName,
			TypeVariant typeVariant,
			ComplexType<?, ?, ?> type) {
	
		this(file, scopedName, typeVariant, type, null, null, null);
	}

	@Override
	public ComplexType<?, ?, ?> getType() {
		return type;
	}
	
	@Override
	public TypeName getTypeName() {
		return type.getCompleteName().toTypeName();
	}

	@Override
	public BuiltinType getBuiltinType() {
		return null;
	}

	@Override
	public Collection<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> getNestedTypes() {
		return nestedTypes;
	}

	@Override
	public Collection<ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName>> getExtendsFrom() {
		return extendsFrom;
	}

	@Override
	public Collection<ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName>> getDependencies() {
		return dependencies;
	}
}
