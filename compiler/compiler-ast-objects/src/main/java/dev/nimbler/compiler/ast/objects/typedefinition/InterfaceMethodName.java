package dev.nimbler.compiler.ast.objects.typedefinition;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.CallableName;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class InterfaceMethodName extends CallableName {

	public InterfaceMethodName(Context context, String name) {
		super(context, name);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_METHOD_NAME;
	}
}
