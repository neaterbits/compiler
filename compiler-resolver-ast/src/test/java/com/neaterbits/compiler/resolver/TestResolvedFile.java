package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.UserDefinedType;

public class TestResolvedFile extends BaseTestFile<ResolvedType<BuiltinType, UserDefinedType, TypeName>>
		implements ResolvedFile<BuiltinType, UserDefinedType, TypeName> {

	@SafeVarargs
	public TestResolvedFile(String name, ResolvedType<BuiltinType, UserDefinedType, TypeName> ... resolvedTypes) {
		super(name, resolvedTypes);
	}
}
