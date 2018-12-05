package com.neaterbits.compiler.bytecode.common.executor;


import com.neaterbits.compiler.bytecode.common.ClassBytecode;

final class BaseTypeClass extends BuiltinClass implements ClassBytecode {

	BaseTypeClass(LanguageClassTypes languageClassTypes) {
		super(languageClassTypes.getBaseTypeMethods());
	}
}
