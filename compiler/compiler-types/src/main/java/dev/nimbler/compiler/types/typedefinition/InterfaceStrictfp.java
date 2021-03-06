package dev.nimbler.compiler.types.typedefinition;

public final class InterfaceStrictfp implements InterfaceModifier {

	@Override
	public <T, R> R visit(InterfaceModifierVisitor<T, R> visitor, T param) {
		return visitor.onStrictfp(this, param);
	}
}
