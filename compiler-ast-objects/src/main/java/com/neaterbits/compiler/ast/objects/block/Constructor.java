package com.neaterbits.compiler.ast.objects.block;

import java.util.List;

import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.objects.typedefinition.ConstructorName;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

public final class Constructor extends CallableCode<ConstructorName> {

	public Constructor(
			Context context,
			ConstructorName name,
			List<Parameter> parameters,
			List<TypeReference> thrownExceptions,
			Block block) {
		
		super(context, null, null, name, parameters, thrownExceptions, block);
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
