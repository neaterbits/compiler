package dev.nimbler.compiler.types.typedefinition;

public final class FieldStatic implements FieldModifier {

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onStatic(this, param);
	}
}
