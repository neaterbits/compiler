package com.neaterbits.exe.vm.bytecode.loader;

import java.util.Objects;

final class TriggeringLoadMethodRequestInfo {

	private final int threadNo;
	private final Object notifyObject;
	private final int type;
	private final int methodNo;
	private final int methodIdx;

	TriggeringLoadMethodRequestInfo(int threadNo, Object notifyObject, int type,
			int methodNo, int methodIdx) {

		Objects.requireNonNull(notifyObject);
		
		this.threadNo = threadNo;
		this.notifyObject = notifyObject;
		this.type = type;
		this.methodNo = methodNo;
		this.methodIdx = methodIdx;
	}

	final int getThreadNo() {
		return threadNo;
	}

	final Object getNotifyObject() {
		return notifyObject;
	}

	final int getType() {
		return type;
	}

	final int getMethodNo() {
		return methodNo;
	}

	final int getMethodIdx() {
		return methodIdx;
	}
}
