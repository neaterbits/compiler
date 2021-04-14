package com.neaterbits.compiler.ast.objects.block;


import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.objects.generics.NamedGenericTypeParameter;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

public final class ClassMethod extends CallableCode<MethodName> {

	public ClassMethod(
	        Context context,
	        List<NamedGenericTypeParameter> genericTypes,
	        TypeReference returnType,
	        String name,
	        Context nameContext,
	        List<Parameter> parameters,
	        List<TypeReference> thrownExceptions,
	        Block block) {
	    
		super(context, genericTypes, returnType, new MethodName(nameContext, name), parameters, thrownExceptions, block);

		Objects.requireNonNull(returnType);
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_METHOD;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onClassMethod(this, param);
	}
}
