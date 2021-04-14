package com.neaterbits.ide.component.common.language.types;

import com.neaterbits.compiler.model.common.IType;
import com.neaterbits.compiler.model.common.VariableScope;

public interface VariableDeclaration {

	String getName();
	
	IType getType();
	
	VariableScope getScope();
}
