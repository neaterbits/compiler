package com.neaterbits.compiler.java;


import com.neaterbits.compiler.language.java.JavaTypes;
import com.neaterbits.compiler.model.objects.ObjectImportsModel;

public class JavaImportsModel extends ObjectImportsModel {

	public JavaImportsModel() {
		super(JavaTypes.getImplicitImports());
	}
}
