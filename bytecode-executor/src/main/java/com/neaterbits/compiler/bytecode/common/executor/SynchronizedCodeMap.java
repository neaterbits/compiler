package com.neaterbits.compiler.bytecode.common.executor;

import java.util.Objects;

import com.neaterbits.compiler.codemap.CodeMap;
import com.neaterbits.compiler.codemap.MethodInfo;
import com.neaterbits.compiler.codemap.MethodVariant;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.codemap.VTableScratchArea;

class SynchronizedCodeMap implements CodeMap {

	private final CodeMap delegate;

	SynchronizedCodeMap(CodeMap delegate) {

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
