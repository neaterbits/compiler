package com.neaterbits.compiler.bytecode.common.loader;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.FieldType;
import com.neaterbits.compiler.util.TypeName;

final class ScanLoadMethodRequest extends NameLoadMethodRequest {
	private final int fromMethodNo;
	private final TriggeringLoadMethodRequestInfo triggeringLoadRequestInfo;
	
	ScanLoadMethodRequest(
			int fromMethodNo,
			TypeName typeName,
			String methodName,
			List<FieldType> parameterTypes,
			TriggeringLoadMethodRequestInfo triggeringLoadRequestInfo) {

		super(typeName, methodName, parameterTypes);
		
		Objects.requireNonNull(triggeringLoadRequestInfo);

		this.fromMethodNo = fromMethodNo;
		this.triggeringLoadRequestInfo = triggeringLoadRequestInfo;
	}

	@Override
	int getCallingMethod() {
		return fromMethodNo;
	}

	final TriggeringLoadMethodRequestInfo getTriggeringLoadRequestInfo() {
		return triggeringLoadRequestInfo;
	}
}
