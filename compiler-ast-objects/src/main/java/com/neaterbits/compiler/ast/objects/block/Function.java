package com.neaterbits.compiler.ast.objects.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.objects.generics.NamedGenericTypeParameter;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class Function extends CallableCode<FunctionName> {

	private final FunctionQualifiers qualifiers;
	
	public Function(
	        Context context,
	        List<NamedGenericTypeParameter> genericTypes,
	        FunctionQualifiers qualifiers,
	        TypeReference returnType,
	        FunctionName name,
	        List<Parameter> parameters,
	        List<TypeReference> thrownExceptions,
	        Block block) {
	    
		super(context, genericTypes, returnType, name, parameters, thrownExceptions, block);
		
		Objects.requireNonNull(returnType);
		Objects.requireNonNull(qualifiers);
		
		this.qualifiers = qualifiers;
	}
	
	public FunctionQualifiers getQualifiers() {
		return qualifiers;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FUNCTION;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onFunction(this, param);
	}
}
