package com.neaterbits.compiler.ast;

import com.neaterbits.compiler.ast.block.ASTName;
import com.neaterbits.compiler.ast.typedefinition.FieldName;
import com.neaterbits.compiler.ast.variables.VarNameDeclaration;
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
