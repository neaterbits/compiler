package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.TypeName;

public class TestResolvedFile extends BaseTestFile<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>>
		implements ResolvedFile<BuiltinType, ComplexType<?, ?, ?>, TypeName> {

	@SafeVarargs
	public TestResolvedFile(String name, ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> ... resolvedTypes) {
		super(name, resolvedTypes);
	}
}
