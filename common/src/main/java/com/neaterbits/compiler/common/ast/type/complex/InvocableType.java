package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexTypeDefinition;

public abstract class InvocableType<T extends ComplexTypeDefinition> extends ComplexType<T> {

	protected InvocableType(CompleteName name, boolean nullable, T definition) {
		super(name, nullable, definition);
	}
}
