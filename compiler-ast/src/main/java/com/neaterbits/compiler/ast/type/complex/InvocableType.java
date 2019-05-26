package com.neaterbits.compiler.ast.type.complex;

import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.typedefinition.ComplexTypeDefinition;
import com.neaterbits.compiler.ast.typedefinition.DeclarationName;
import com.neaterbits.compiler.util.name.BaseTypeName;

public abstract class InvocableType<
	NAME extends BaseTypeName,
	DECLARATION_NAME extends DeclarationName<NAME>,
	T extends ComplexTypeDefinition<NAME, DECLARATION_NAME>> extends ComplexType<NAME, DECLARATION_NAME, T> {

	protected InvocableType(CompleteName name, boolean nullable, T definition) {
		super(name, nullable, definition);
	}
}
