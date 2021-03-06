package dev.nimbler.compiler.types.typedefinition;

public enum ClassVisibility implements ClassModifier {
	PUBLIC,
	PRIVATE,
	NAMESPACE;

	@Override
	public <T, R> R visit(ClassModifierVisitor<T, R> visitor, T param) {
		return visitor.onClassVisibility(this, param);
	}
}
