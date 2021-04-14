package dev.nimbler.compiler.types.statement;

import java.util.Objects;

import dev.nimbler.compiler.types.typedefinition.FieldModifier;
import dev.nimbler.compiler.types.typedefinition.FieldModifierVisitor;
import dev.nimbler.compiler.types.typedefinition.VariableModifier;
import dev.nimbler.compiler.types.typedefinition.VariableModifierVisitor;
import dev.nimbler.language.common.types.Mutability;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mutability == null) ? 0 : mutability.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ASTMutability other = (ASTMutability) obj;
        if (mutability != other.mutability)
            return false;
        return true;
    }
}
