package com.neaterbits.compiler.ast.objects;

import com.neaterbits.compiler.ast.objects.block.ASTName;
import com.neaterbits.compiler.ast.objects.typedefinition.FieldName;
import com.neaterbits.compiler.ast.objects.variables.VarNameDeclaration;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
