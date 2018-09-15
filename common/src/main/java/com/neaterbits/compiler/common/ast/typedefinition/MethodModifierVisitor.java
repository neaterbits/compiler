package com.neaterbits.compiler.common.ast.typedefinition;

public interface MethodModifierVisitor<T, R> {
	
	R onVisibility(MethodVisibility visibility, T param);
	R onOverride(MethodOverride methodOverride, T param);
	R onStatic(MethodStatic methodStatic, T param);
	R onStrictFp(MethodStrictfp methodStrictfp, T param);
	R onSynchronized(MethodSynchronized methodSynchronized, T param);
	R onNative(MethodNative methodNative, T param);

}
