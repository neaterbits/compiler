package dev.nimbler.language.codemap;

import java.util.Objects;

import dev.nimbler.language.common.types.MethodVariant;
import dev.nimbler.language.common.types.Mutability;
import dev.nimbler.language.common.types.TypeVariant;
import dev.nimbler.language.common.types.Visibility;

public class SynchronizedCodeMap implements CodeMap {

	private final CodeMap delegate;

	protected SynchronizedCodeMap(CodeMap delegate) {

		Objects.requireNonNull(delegate);
		
		this.delegate = delegate;
	}

	@Override
	public final synchronized int addType(TypeVariant typeVariant, int[] extendsFromClasses, int [] extendsFromInterfaces) {
		return delegate.addType(typeVariant, extendsFromClasses, extendsFromInterfaces);
	}
	
	@Override
	public final synchronized TypeVariant getTypeVariantForType(int typeNo) {
		return delegate.getTypeVariantForType(typeNo);
	}

	@Override
	public final synchronized int getExtendsFromSingleSuperClass(int type) {
		return delegate.getExtendsFromSingleSuperClass(type);
	}

	@Override
	public final synchronized int addField(int type, String name, int fieldType, boolean isStatic, Visibility visibility,
			Mutability mutability, boolean isVolatile, boolean isTransient, int indexInType) {
		return delegate.addField(type, name, fieldType, isStatic, visibility, mutability, isVolatile, isTransient, indexInType);
	}

	@Override
	public final synchronized FieldInfo getFieldInfo(int typeNo, String fieldName) {
		return delegate.getFieldInfo(typeNo, fieldName);
	}

	@Override
	public final synchronized void setMethodCount(int type, int methodCount) {
		delegate.setMethodCount(type, methodCount);
	}

	@Override
	public final synchronized int addOrGetMethod(int type, String methodName, MethodVariant methodVariant, int returnType, int[] parameters,
			int indexInType) {
		return delegate.addOrGetMethod(type, methodName, methodVariant, returnType, parameters, indexInType);
	}

	@Override
	public final synchronized int addOrGetExtendingMethod(int extendedMethod, MethodVariant extendedMethodVariant, int type,
			String methodName, MethodVariant methodVariant, int returnType, int[] parameters, int indexInType) {

		return delegate.addOrGetExtendingMethod(extendedMethod, extendedMethodVariant, type, methodName, methodVariant, returnType, parameters, indexInType);
	}

	@Override
	public final synchronized void computeMethodExtends(int typeNo) {
		delegate.computeMethodExtends(typeNo);
	}

	@Override
	public final synchronized int[] getTypesThisDirectlyExtends(int typeNo) {
		return delegate.getTypesThisDirectlyExtends(typeNo);
	}

	@Override
	public final synchronized int[] getTypesDirectlyExtendingThis(int type) {
		return delegate.getTypesDirectlyExtendingThis(type);
	}
	@Override
	public final synchronized int[] getAllTypesExtendingThis(int typeNo) {
		return delegate.getAllTypesExtendingThis(typeNo);
	}

	@Override
	public final synchronized int getTypeForMethod(int methodNo) {
		return delegate.getTypeForMethod(methodNo);
	}

	@Override
	public final synchronized int getIndexForMethod(int methodNo) {
		return delegate.getIndexForMethod(methodNo);
	}

	@Override
	public final synchronized MethodInfo getMethodInfo(int typeNo, String methodName, int[] parameterTypes) {
		return delegate.getMethodInfo(typeNo, methodName, parameterTypes);
	}

	@Override
	public final synchronized int getDistinctMethodCount(int typeNo, MethodFilter methodFilter, VTableScratchArea scratchArea) {
		return delegate.getDistinctMethodCount(typeNo, methodFilter, scratchArea);
	}
}
