package dev.nimbler.exe.vm.bytecode.executor;

import dev.nimbler.exe.vm.bytecode.loader.codemap.LoaderCodeMap;
import dev.nimbler.language.codemap.SynchronizedCodeMap;

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
