package com.neaterbits.exe.vm.bytecode.executor;

import com.neaterbits.language.bytecode.common.ClassBytecode;

final class BaseTypeClass extends BuiltinClass implements ClassBytecode {

	BaseTypeClass(LanguageClassTypes languageClassTypes) {
		super(languageClassTypes.getBaseTypeMethods());
	}
}
