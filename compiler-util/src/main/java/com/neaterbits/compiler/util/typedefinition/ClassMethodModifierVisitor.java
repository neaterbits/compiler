package com.neaterbits.compiler.util.typedefinition;

public interface ClassMethodModifierVisitor<T, R> {
	
	R onVisibility(ClassMethodVisibility visibility, T param);
	R onOverride(ClassMethodOverride methodOverride, T param);
	R onStatic(ClassMethodStatic methodStatic, T param);
	R onStrictFp(ClassMethodStrictfp methodStrictfp, T param);
	R onSynchronized(ClassMethodSynchronized methodSynchronized, T param);
	R onNative(ClassMethodNative methodNative, T param);

}
