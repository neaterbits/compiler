package dev.nimbler.compiler.ast.objects.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.generics.NamedGenericTypeParameter;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

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
