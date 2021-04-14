package dev.nimbler.exe.vm.bytecode.loader;

import java.util.Objects;

final class ExecutionPathLoadMethodRequest extends LoadMethodRequest {

	private final int methodNo;
	private final int threadNo;
	private final Object notifyObject;

	ExecutionPathLoadMethodRequest(int methodNo, int threadNo, Object notifyObject) {
		
		Objects.requireNonNull(notifyObject);
		
		this.methodNo = methodNo;
		this.threadNo = threadNo;
		this.notifyObject = notifyObject;
	}

	final int getMethodNo() {
		return methodNo;
	}

	final int getThreadNo() {
		return threadNo;
	}

	final Object getNotifyObject() {
		return notifyObject;
	}
}
