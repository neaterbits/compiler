package com.neaterbits.compiler.types.typedefinition;

public interface ClassModifierVisitor<T, R> {

	R onClassVisibility(ClassVisibility visibility, T param);
	R onSubclassing(Subclassing subclassing, T param);
	R onStatic(ClassStatic classStatic, T param);
	R onStrictFp(ClassStrictfp classStrictfp, T param);

}
