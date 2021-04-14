package dev.nimbler.exe.vm.bytecode.loader;

import java.util.List;
import java.util.Objects;

import dev.nimbler.language.common.types.FieldType;
import dev.nimbler.language.common.types.TypeName;

final class InitialLoadMethodRequest extends NameLoadMethodRequest {

	private final int threadNo;
	private final Object notifyObject;

	InitialLoadMethodRequest(
			TypeName typeName,
			String methodName,
			List<FieldType> parameterTypes,
			int threadNo,
			Object notifyObject) {
		
		super(typeName, methodName, parameterTypes);

		Objects.requireNonNull(notifyObject);

		this.threadNo = threadNo;
		this.notifyObject = notifyObject;
	}

	int getThreadNo() {
		return threadNo;
	}

	Object getNotifyObject() {
		return notifyObject;
	}

	@Override
	int getCallingMethod() {
		return -1;
	}
}
