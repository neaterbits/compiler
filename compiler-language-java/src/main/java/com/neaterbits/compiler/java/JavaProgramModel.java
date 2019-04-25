package com.neaterbits.compiler.java;


import com.neaterbits.compiler.resolver.ast.model.ObjectProgramModel;
import com.neaterbits.compiler.util.model.FieldModifiers;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.Visibility;

public class JavaProgramModel extends ObjectProgramModel {

	public JavaProgramModel() {
		super(JavaTypes.getImplicitImports(), new FieldModifiers(false, Visibility.NAMESPACE, Mutability.MUTABLE, false, false));
	}
}
