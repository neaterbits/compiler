package com.neaterbits.compiler.common.ast.block;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorName;

public final class Constructor extends CallableCode<ConstructorName> {

	public Constructor(
			Context context,
			ConstructorName name,
			List<Parameter> parameters,
			Block block) {
		
		super(context, null, name, parameters, block);
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onConstructor(this, param);
	}
}
