package dev.nimbler.compiler.ast.objects.block;

import java.util.List;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.typedefinition.ConstructorName;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

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
