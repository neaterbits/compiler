package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.typedefinition.FieldModifier;
import com.neaterbits.compiler.ast.typedefinition.FieldModifierVisitor;
import com.neaterbits.compiler.ast.typedefinition.VariableModifier;
import com.neaterbits.compiler.ast.typedefinition.VariableModifierVisitor;
import com.neaterbits.compiler.util.model.Mutability;

public class ASTMutability implements VariableModifier, FieldModifier {

	private final Mutability mutability;
	
	public ASTMutability(Mutability mutability) {

		Objects.requireNonNull(mutability);
		
		this.mutability = mutability;
	}
	
	public Mutability getMutability() {
		return mutability;
	}

	@Override
	public <T, R> R visit(VariableModifierVisitor<T, R> visitor, T param) {
		return visitor.onVariableMutability(this, param);
	}

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onFieldMutability(this, param);
	}
}
