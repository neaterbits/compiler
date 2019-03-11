package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;

public class TestResolvedFile extends BaseTestFile<ResolvedType<BuiltinType, ComplexType<?, ?, ?>>>
		implements ResolvedFile<BuiltinType, ComplexType<?, ?, ?>> {

	@SafeVarargs
	public TestResolvedFile(String name, ResolvedType<BuiltinType, ComplexType<?, ?, ?>> ... resolvedTypes) {
		super(name, resolvedTypes);
	}
}
