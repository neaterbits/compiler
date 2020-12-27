package com.neaterbits.compiler.java;

import com.neaterbits.compiler.language.java.JavaLanguageSpec;
import com.neaterbits.compiler.model.objects.ObjectProgramModel;

public class JavaProgramModel extends ObjectProgramModel {

    public JavaProgramModel() {
        super(JavaLanguageSpec.INSTANCE);
    }
}
