package com.neaterbits.compiler.common.emit.base;

import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.Method;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InnerClassMember;
import com.neaterbits.compiler.common.ast.typedefinition.MethodMember;
import com.neaterbits.compiler.common.emit.EmitterState;

public abstract class BaseProceduralProgramEmitter<T extends EmitterState> extends BaseCompilationUnitEmitter<T> {

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

	@Override
	public final Void onInnerClassMember(InnerClassMember field, T param) {
		throw new UnsupportedOperationException();
	}
}
