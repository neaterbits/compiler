package com.neaterbits.compiler.parser.listener.stackbased.state.setters;


public interface VariableModifierSetter<MODIFIER_HOLDER> {

	void addModifier(MODIFIER_HOLDER modifier);
	
}
