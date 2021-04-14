package dev.nimbler.compiler.emit.base;

import dev.nimbler.compiler.ast.objects.Namespace;
import dev.nimbler.compiler.ast.objects.block.ClassMethod;
import dev.nimbler.compiler.ast.objects.block.Constructor;
import dev.nimbler.compiler.ast.objects.block.StaticInitializer;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ConstructorMember;
import dev.nimbler.compiler.ast.objects.typedefinition.InnerClassMember;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethod;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethodMember;
import dev.nimbler.compiler.emit.EmitterState;

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
