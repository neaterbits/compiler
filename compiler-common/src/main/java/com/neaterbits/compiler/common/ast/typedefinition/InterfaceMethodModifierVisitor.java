package com.neaterbits.compiler.common.ast.typedefinition;

public interface InterfaceMethodModifierVisitor<T, R> {

	R onVisibility(InterfaceMethodVisibility visibility, T param);
	R onAbstract(InterfaceMethodAbstract methodAbstract, T param);
	R onDefault(InterfaceMethodDefault methodDefault, T param);
	R onStatic(InterfaceMethodStatic methodStatic, T param);
	R onStrictfp(InterfaceMethodStrictfp methodStrictfp, T param);

}
