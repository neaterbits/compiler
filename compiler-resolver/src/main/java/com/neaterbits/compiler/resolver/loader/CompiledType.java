package com.neaterbits.compiler.resolver.loader;

import java.util.Collection;

import com.neaterbits.compiler.ast.NamespaceReference;
import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.complex.ComplexType;

public interface CompiledType extends LoaderTypeInfo {

	TypeSpec getSpec();
	
	FileSpec getFile();
	
	default NamespaceReference getNamespace() {
		return getCompleteName().getNamespace();
	}

	default CompleteName getCompleteName() {
		return getType().getCompleteName();
	}

	
	ComplexType<?, ?, ?> getType();
	
	Collection<CompiledType> getNestedTypes();

	Collection<CompiledTypeDependency> getExtendsFrom();
	
	Collection<CompiledTypeDependency> getDependencies();

}
