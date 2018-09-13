package com.neaterbits.compiler.common.emit;

import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.Method;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.MethodMember;

public abstract class BaseProceduralProgramEmitter<T extends EmitterState> extends BaseProgramEmitter<T> {

	@Override
	public final Void onClassDefinition(ClassDefinition classDefinition, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onMethod(Method method, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onMethodMember(MethodMember method, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onClassDataFieldMember(ClassDataFieldMember field, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onNamespace(Namespace namespace, EmitterState param) {
		throw new UnsupportedOperationException();
	}
}
