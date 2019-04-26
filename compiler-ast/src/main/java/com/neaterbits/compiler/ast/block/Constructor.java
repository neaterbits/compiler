package com.neaterbits.compiler.ast.block;

import java.util.List;

import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.typedefinition.ConstructorName;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class Constructor extends CallableCode<ConstructorName> {

	public Constructor(
			Context context,
			ConstructorName name,
			List<Parameter> parameters,
			Block block) {
		
		super(context, null, name, parameters, block);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CONSTRUCTOR;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onConstructor(this, param);
	}
}
