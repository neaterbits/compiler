package com.neaterbits.compiler.java;


import com.neaterbits.compiler.language.java.JavaLanguageSpec;
import com.neaterbits.compiler.resolver.ast.objects.model.ObjectProgramModel;
import com.neaterbits.compiler.util.FullContextProvider;

public class JavaProgramModel extends ObjectProgramModel {

    public JavaProgramModel() {
        this(null);
    }
    
	public JavaProgramModel(FullContextProvider fullContextProvider) {
		super(fullContextProvider, JavaLanguageSpec.INSTANCE);
	}
}
