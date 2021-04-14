package dev.nimbler.language.bytecode.common;

import dev.nimbler.language.common.types.FieldType;
import dev.nimbler.language.common.types.MethodVariant;

public abstract class BaseClassFile implements ClassBytecode {

	@Override
	public final int getStaticFieldCount() {
		
		int numStaticFields = 0;
		
		for (int i = 0; i < getFieldCount(); ++ i) {
			if (isStatic(i)) {
				++ numStaticFields;
			}
		}
		
		return numStaticFields;
	}

	@Override
	public final FieldType getStaticFieldType(int index) {

		int staticIndex = 0;
		
		FieldType fieldType = null;
		
		for (int i = 0; i < getFieldCount(); ++ i) {
			if (isStatic(i)) {
				
				if (staticIndex == index) {
					fieldType = getFieldType(i);
					break;
				}
				
				++ staticIndex;
			}
		}
		
		return fieldType;
	}

	@Override
	public final int getStaticMethodCount() {

		int numStaticMethods = 0;
		
		for (int i = 0; i < getFieldCount(); ++ i) {
			if (getMethodVariant(i) == MethodVariant.STATIC) {
				++ numStaticMethods;
			}
		}
		
		return numStaticMethods;
	}
	
}
