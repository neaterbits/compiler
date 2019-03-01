package com.neaterbits.compiler.common.ast.type.complex;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.BaseTypeName;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.ResolvableType;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexTypeDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.DataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.DeclarationName;
import com.neaterbits.compiler.common.ast.typedefinition.FieldName;

public abstract class ComplexType<
					NAME extends BaseTypeName,
					DECLARATION_NAME extends DeclarationName<NAME>,
					T extends ComplexTypeDefinition<NAME, DECLARATION_NAME>>

extends ResolvableType {

	private final T definition;

	public ComplexType(CompleteName name, boolean nullable, T definition) {
		super(name, nullable);
		
		Objects.requireNonNull(definition);

		this.definition = definition;
	}

	public final BaseType getFieldType(FieldName fieldName) {
		
		Objects.requireNonNull(fieldName);
		
		for (ComplexMemberDefinition member : getMembers()) {
			if (member instanceof DataFieldMember) {
				final DataFieldMember dataFieldMember = (DataFieldMember)member;

				if (dataFieldMember.getName().equals(fieldName)) {
					return dataFieldMember.getType().getType();
				}
			}
		}
		
		return null;
	}
	
	public final T getDefinition() {
		return definition;
	}
	
	public final ASTList<ComplexMemberDefinition> getMembers() {
		return definition.getMembers();
	}
}
