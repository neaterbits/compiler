package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.block.Callable;
import dev.nimbler.compiler.ast.objects.block.Parameter;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class InterfaceMethod extends Callable<InterfaceMethodName> {

	public InterfaceMethod(Context context, TypeReference returnType, String name, Context nameContext,
			List<Parameter> parameters) {
		super(context, returnType, new InterfaceMethodName(nameContext, name), parameters);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_METHOD;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onInterfaceMethod(this, param);
	}
}
