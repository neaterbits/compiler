package dev.nimbler.compiler.ast.objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.ASTName;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldName;
import dev.nimbler.compiler.ast.objects.variables.VarNameDeclaration;
import dev.nimbler.compiler.types.ParseTreeElement;

@Deprecated
public final class FieldNameDeclaration extends ASTName {

	public FieldNameDeclaration(VarNameDeclaration varNameDeclaration) {
		this(varNameDeclaration.getContext(), varNameDeclaration.getVarName().getName());
	}

	public FieldNameDeclaration(Context context, String name) {
		super(context, name);
	}

	public FieldName toFieldName() {
		return new FieldName(getName());
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FIELD_NAME_DECLARATION;
	}
}
