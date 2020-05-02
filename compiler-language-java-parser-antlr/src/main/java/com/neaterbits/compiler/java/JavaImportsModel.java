package com.neaterbits.compiler.java;


import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaTypes;
import com.neaterbits.compiler.resolver.ast.objects.model.ObjectImportsModel;

public class JavaImportsModel extends ObjectImportsModel {

	public JavaImportsModel() {
		super(JavaTypes.getImplicitImports());
	}
}
