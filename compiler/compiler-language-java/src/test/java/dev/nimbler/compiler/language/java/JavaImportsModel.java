package dev.nimbler.compiler.language.java;


import dev.nimbler.compiler.model.objects.ObjectImportsModel;

public class JavaImportsModel extends ObjectImportsModel {

	public JavaImportsModel() {
		super(JavaTypes.getImplicitImports());
	}
}
