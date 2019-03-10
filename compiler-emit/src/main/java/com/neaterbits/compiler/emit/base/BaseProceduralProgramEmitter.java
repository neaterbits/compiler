package com.neaterbits.compiler.emit.base;

import com.neaterbits.compiler.ast.Namespace;
import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.block.Constructor;
import com.neaterbits.compiler.ast.block.StaticInitializer;
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ConstructorMember;
import com.neaterbits.compiler.ast.typedefinition.InnerClassMember;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethod;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodMember;
import com.neaterbits.compiler.emit.EmitterState;

public abstract class BaseProceduralProgramEmitter<T extends EmitterState> extends BaseCompilationUnitEmitter<T> {

	@Override
	public final Void onClassDefinition(ClassDefinition classDefinition, EmitterState param) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public final Void onStaticInitializer(StaticInitializer initializer, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onConstructor(Constructor constructor, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onConstructorMember(ConstructorMember constructor, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onClassMethod(ClassMethod method, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onClassMethodMember(ClassMethodMember method, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onClassDataFieldMember(ClassDataFieldMember field, EmitterState param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onInterfaceDefinition(InterfaceDefinition interfaceDefinition, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onInterfaceMethod(InterfaceMethod method, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public final Void onInterfaceMethodMember(InterfaceMethodMember method, T param) {
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
