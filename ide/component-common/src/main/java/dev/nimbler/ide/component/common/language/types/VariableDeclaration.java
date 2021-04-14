package dev.nimbler.ide.component.common.language.types;

import dev.nimbler.compiler.model.common.IType;
import dev.nimbler.compiler.model.common.VariableScope;

public interface VariableDeclaration {

	String getName();
	
	IType getType();
	
	VariableScope getScope();
}
