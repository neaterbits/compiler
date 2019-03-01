package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.type.BaseTypeName;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexTypeDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.DeclarationName;

public abstract class InvocableType<
	NAME extends BaseTypeName,
	DECLARATION_NAME extends DeclarationName<NAME>,
	T extends ComplexTypeDefinition<NAME, DECLARATION_NAME>> extends ComplexType<NAME, DECLARATION_NAME, T> {

	protected InvocableType(CompleteName name, boolean nullable, T definition) {
		super(name, nullable, definition);
	}
}
