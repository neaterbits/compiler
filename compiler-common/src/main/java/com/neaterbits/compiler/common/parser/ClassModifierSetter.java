package com.neaterbits.compiler.common.parser;

import com.neaterbits.compiler.common.ast.typedefinition.ClassModifierHolder;

public interface ClassModifierSetter {

	void addClassModifier(ClassModifierHolder modifier);
	
}
