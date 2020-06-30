package com.neaterbits.compiler.util.statement;

import java.util.Objects;

import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.typedefinition.FieldModifier;
import com.neaterbits.compiler.util.typedefinition.FieldModifierVisitor;
import com.neaterbits.compiler.util.typedefinition.VariableModifier;
import com.neaterbits.compiler.util.typedefinition.VariableModifierVisitor;

public class ASTMutability implements VariableModifier, FieldModifier {

    public static final ASTMutability MUTABLE = new ASTMutability(Mutability.MUTABLE);
    public static final ASTMutability VALUE_OR_REF_IMMUTABLE = new ASTMutability(Mutability.VALUE_OR_REF_IMMUTABLE);
    public static final ASTMutability VALUE_OR_OBJECT_IMMUTABLE = new ASTMutability(Mutability.VALUE_OR_OBJECT_IMMUTABLE);
    public static final ASTMutability VALUE_OR_OBJECT_OR_REF_IMMUTABLE = new ASTMutability(Mutability.VALUE_OR_OBJECT_OR_REF_IMMUTABLE);

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
