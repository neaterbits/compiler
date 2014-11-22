package com.neaterbits.compiler.common.ast.type.complex;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexTypeDefinition;

public abstract class ComplexType<T extends ComplexTypeDefinition> extends NamedType {

	private final T definition;

	public ComplexType(CompleteName name, boolean nullable, T definition) {
		super(name, nullable);
		
		Objects.requireNonNull(definition);

		this.definition = definition;
	}
	
	public final T getDefinition() {
		return definition;
	}
	
	public final ASTList<ComplexMemberDefinition> getMembers() {
		return definition.getMembers();
	}
}
