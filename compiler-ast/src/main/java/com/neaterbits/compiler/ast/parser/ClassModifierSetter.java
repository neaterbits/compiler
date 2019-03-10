package com.neaterbits.compiler.ast.parser;

import com.neaterbits.compiler.ast.typedefinition.ClassModifierHolder;

public interface ClassModifierSetter {

	void addClassModifier(ClassModifierHolder modifier);
	
}
