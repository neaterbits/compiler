package dev.nimbler.compiler.ast.objects.type.complex;

import java.util.Objects;

import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.ResolvableType;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexMemberDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexTypeDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.DeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldName;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.util.name.BaseTypeName;

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

	public final TypeReference getFieldType(FieldName fieldName) {
		
		Objects.requireNonNull(fieldName);
		
		for (ComplexMemberDefinition member : getMembers()) {
			if (member instanceof ClassDataFieldMember) {
				final ClassDataFieldMember dataFieldMember = (ClassDataFieldMember)member;
				
				for (InitializerVariableDeclarationElement element : dataFieldMember.getInitializers()) {

    				if (element.getNameString().equals(fieldName.getName())) {
    					return dataFieldMember.getType();
    				}
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
