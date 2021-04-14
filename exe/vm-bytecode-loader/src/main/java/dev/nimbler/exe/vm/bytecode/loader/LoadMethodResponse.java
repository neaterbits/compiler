package dev.nimbler.exe.vm.bytecode.loader;

import java.util.Objects;

final class LoadMethodResponse<METHOD> {
	private final TriggeringLoadMethodRequestInfo requestInfo;
	private final METHOD compiledMethod;

	LoadMethodResponse(TriggeringLoadMethodRequestInfo requestInfo, METHOD compiledMethod) {
		
		Objects.requireNonNull(requestInfo);
		Objects.requireNonNull(compiledMethod);
		
		this.requestInfo = requestInfo;
		this.compiledMethod = compiledMethod;
	}

	TriggeringLoadMethodRequestInfo getRequestInfo() {
		return requestInfo;
	}

	METHOD getCompiledMethod() {
		return compiledMethod;
	}
}
