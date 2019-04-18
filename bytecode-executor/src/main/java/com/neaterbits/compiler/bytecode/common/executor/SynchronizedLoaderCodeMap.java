package com.neaterbits.compiler.bytecode.common.executor;

import com.neaterbits.compiler.bytecode.common.loader.codemap.LoaderCodeMap;

final class SynchronizedLoaderCodeMap extends SynchronizedCodeMap implements LoaderCodeMap {

	private final LoaderCodeMap delegate;
	
	SynchronizedLoaderCodeMap(LoaderCodeMap delegate) {
		super(delegate);

		this.delegate = delegate;
	}

	@Override
	public final synchronized void addMethodCall(int calledFrom, int calledTo) {
		
		delegate.addMethodCall(calledFrom, calledTo);
	}

	@Override
	public final synchronized int recurseCallGraph(int fromMethodNo, MethodRef methodRef) {
		return delegate.recurseCallGraph(fromMethodNo, methodRef);
	}
}
