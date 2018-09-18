package com.neaterbits.compiler.common.parser;

import com.neaterbits.compiler.common.ast.typedefinition.VariableModifierHolder;

public interface VariableModifierSetter {

	void addModifier(VariableModifierHolder modifier);
	
}
