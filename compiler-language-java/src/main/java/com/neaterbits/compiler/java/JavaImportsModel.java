package com.neaterbits.compiler.java;


import com.neaterbits.compiler.resolver.ast.model.ObjectImportsModel;

public class JavaImportsModel extends ObjectImportsModel {

	public JavaImportsModel() {
		super(JavaTypes.getImplicitImports());
	}
}
