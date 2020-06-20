package com.neaterbits.compiler.java;


import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaTypes;
import com.neaterbits.compiler.resolver.ast.objects.model.ObjectProgramModel;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.model.FieldModifiers;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.Visibility;

public class JavaProgramModel extends ObjectProgramModel {

    public JavaProgramModel() {
        this(null);
    }
    
	public JavaProgramModel(FullContextProvider fullContextProvider) {
		super(fullContextProvider, JavaTypes.getImplicitImports(), new FieldModifiers(false, Visibility.NAMESPACE, Mutability.MUTABLE, false, false));
	}
}
