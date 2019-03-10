package com.neaterbits.compiler.ast.parser;

import com.neaterbits.compiler.ast.typedefinition.VariableModifierHolder;

public interface VariableModifierSetter {

	void addModifier(VariableModifierHolder modifier);
	
}
