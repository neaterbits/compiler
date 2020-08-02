package com.neaterbits.compiler.language.java.model.astobjects;


import com.neaterbits.compiler.language.java.JavaTypes;
import com.neaterbits.compiler.resolver.ast.objects.model.ObjectImportsModel;

public class JavaImportsModel extends ObjectImportsModel {

	public JavaImportsModel() {
		super(JavaTypes.getImplicitImports());
	}
}
