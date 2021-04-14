package dev.nimbler.exe.vm.bytecode.executor;

import dev.nimbler.language.bytecode.common.ClassBytecode;

final class BaseTypeClass extends BuiltinClass implements ClassBytecode {

	BaseTypeClass(LanguageClassTypes languageClassTypes) {
		super(languageClassTypes.getBaseTypeMethods());
	}
}
