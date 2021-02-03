package com.neaterbits.compiler.bytecode.common.executor;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;

final class ClassObjectClass extends BuiltinClass implements ClassBytecode {

    ClassObjectClass(LanguageClassTypes languageClassTypes) {

		super(languageClassTypes.getClassTypeMethods());
		
		addField("type", languageClassTypes.getIntType());
		addField("instanceMemorySize", languageClassTypes.getIntType());
		addField("name", languageClassTypes.getStringType());
		addField("simpleName", languageClassTypes.getStringType());
	}
}
