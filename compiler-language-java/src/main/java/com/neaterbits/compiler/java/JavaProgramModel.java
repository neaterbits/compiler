package com.neaterbits.compiler.java;


import com.neaterbits.compiler.resolver.ast.model.ObjectProgramModel;

public class JavaProgramModel extends ObjectProgramModel {

	public JavaProgramModel() {
		super(JavaTypes.getImplicitImports());
	}
}
