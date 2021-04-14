package dev.nimbler.exe.vm.bytecode.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.language.common.types.TypeName;

final class LoadedMethod {
	private List<TypeName> instantiatedTypes;
	private List<InvokedMethod> invokedMethods;
	private boolean processed;
	private Object compiledMethod;
	
	void addInstantiatedType(TypeName typeName) {
		
		Objects.requireNonNull(typeName);
		
		if (instantiatedTypes == null) {
			this.instantiatedTypes = new ArrayList<>();
		}
		
		instantiatedTypes.add(typeName);
	}
	
	boolean isProcessed() {
		return processed;
	}

	void setProcessed(boolean processed) {
		this.processed = processed;
	}
	
	Object getCompiledMethod() {
		return compiledMethod;
	}

	void setCompiledMethod(Object compiledMethod) {
		
		Objects.requireNonNull(compiledMethod);
		
		this.compiledMethod = compiledMethod;
	}

	void addInvokedMethod(InvokedMethod invokedMethod) {
		
		Objects.requireNonNull(invokedMethod);
		
		if (invokedMethods == null) {
			this.invokedMethods = new ArrayList<>();
		}
		
		invokedMethods.add(invokedMethod);
	}

	List<TypeName> getInstantiatedTypes() {
		return instantiatedTypes;
	}

	List<InvokedMethod> getInvokedMethods() {
		return invokedMethods;
	}
}
