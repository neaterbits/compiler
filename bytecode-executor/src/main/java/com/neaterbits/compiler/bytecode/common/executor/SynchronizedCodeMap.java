package com.neaterbits.compiler.bytecode.common.executor;

import java.util.Objects;

import com.neaterbits.compiler.codemap.CodeMap;
import com.neaterbits.compiler.codemap.MethodVariant;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.codemap.VTableScratchArea;

final class SynchronizedCodeMap implements CodeMap {

	private final CodeMap delegate;

	SynchronizedCodeMap(CodeMap delegate) {

		Objects.requireNonNull(delegate);
		
		this.delegate = delegate;
	}

	@Override
	public synchronized int addType(TypeVariant typeVariant, int[] extendsFromClasses, int [] extendsFromInterfaces) {
		return delegate.addType(typeVariant, extendsFromClasses, extendsFromInterfaces);
	}
	
	@Override
	public synchronized int getExtendsFromSingleSuperClass(int type) {
		return delegate.getExtendsFromSingleSuperClass(type);
	}

	@Override
	public synchronized void setMethodCount(int type, int methodCount) {
		delegate.setMethodCount(type, methodCount);
	}

	@Override
	public synchronized int addOrGetMethod(int type, String methodName, MethodVariant methodVariant, int returnType, int[] parameters,
			int indexInType) {
		return delegate.addOrGetMethod(type, methodName, methodVariant, returnType, parameters, indexInType);
	}

	@Override
	public synchronized int addOrGetExtendingMethod(int extendedMethod, MethodVariant extendedMethodVariant, int type,
			String methodName, MethodVariant methodVariant, int returnType, int[] parameters, int indexInType) {

		return delegate.addOrGetExtendingMethod(extendedMethod, extendedMethodVariant, type, methodName, methodVariant, returnType, parameters, indexInType);
	}

	@Override
	public int[] getTypesThisDirectlyExtends(int typeNo) {
		return delegate.getTypesThisDirectlyExtends(typeNo);
	}

	@Override
	public synchronized int[] getTypesDirectlyExtendingThis(int type) {
		return delegate.getTypesDirectlyExtendingThis(type);
	}

	@Override
	public synchronized int getTypeForMethod(int methodNo) {
		return delegate.getTypeForMethod(methodNo);
	}

	@Override
	public synchronized int getIndexForMethod(int methodNo) {
		return delegate.getIndexForMethod(methodNo);
	}

	@Override
	public synchronized void addMethodCall(int calledFrom, int calledTo) {
		delegate.addMethodCall(calledFrom, calledTo);
	}

	@Override
	public synchronized int recurseCallGraph(int fromMethodNo, MethodRef methodRef) {
		return delegate.recurseCallGraph(fromMethodNo, methodRef);
	}

	@Override
	public synchronized int getDistinctMethodCount(int typeNo, MethodFilter methodFilter, VTableScratchArea scratchArea) {
		return delegate.getDistinctMethodCount(typeNo, methodFilter, scratchArea);
	}
}
