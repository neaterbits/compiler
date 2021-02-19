package com.neaterbits.compiler.common.ast.typedefinition;

public interface InterfaceModifierVisitor<T, R> {

	R onVisibility(InterfaceVisibility visibility, T param);
	R onAbstract(InterfaceAbstract interfaceAbstract, T param);
	R onStatic(InterfaceStatic interfaceStatic, T param);
	R onStrictfp(InterfaceStrictfp interfaceStrictfp, T param);

}
