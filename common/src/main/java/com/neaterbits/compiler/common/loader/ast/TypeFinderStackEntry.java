package com.neaterbits.compiler.common.loader.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.resolver.ReferenceType;

final class TypeFinderStackEntry {
	private final BaseASTElement element;
	private final List<String> scope;
	private final boolean mayHaveNestedTypes;
	
	private List<CompiledType> nestedTypes;
	private List<ScopedName> extendsFrom;
	private List<CompiledTypeDependency> dependencies;
	
	TypeFinderStackEntry(BaseASTElement element, List<String> scope, boolean mayHaveNestedTypes) {

		Objects.requireNonNull(element);
		
		this.element = element;
		this.scope = scope;
		this.mayHaveNestedTypes = mayHaveNestedTypes;
	}
	
	BaseASTElement getElement() {
		return element;
	}

	List<String> getScope() {
		return scope;
	}

	boolean mayHaveNestedTypes() {
		return mayHaveNestedTypes;
	}
	
	List<CompiledType> getNestedTypes() {
		return nestedTypes;
	}

	List<ScopedName> getExtendsFrom() {
		return extendsFrom;
	}

	List<CompiledTypeDependency> getDependencies() {
		return dependencies;
	}

	void addNestedType(ParsedType nestedType) {
		Objects.requireNonNull(nestedType);
		
		if (this.nestedTypes == null) {
			this.nestedTypes = new ArrayList<>();
		}
		
		this.nestedTypes.add(nestedType);
	}
	
	void addExtendsFrom(ScopedName extendsFrom) {
		Objects.requireNonNull(extendsFrom);
		
		if (this.extendsFrom == null) {
			this.extendsFrom = new ArrayList<>();
		}
		
		this.extendsFrom.add(extendsFrom);
	}
	
	void addDependency(ScopedName scopedName, ReferenceType type) {

		final ParsedTypeReference dependency = new ParsedTypeReference(scopedName, type);
		
		Objects.requireNonNull(dependency);
		Objects.requireNonNull(type);
		
		if (this.dependencies == null) {
			this.dependencies = new ArrayList<>();
		}
		
		this.dependencies.add(dependency);
	}

	@Override
	public String toString() {
		return element.toString();
	}
}
