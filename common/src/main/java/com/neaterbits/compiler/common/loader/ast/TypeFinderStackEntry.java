package com.neaterbits.compiler.common.loader.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;

import com.neaterbits.compiler.common.ResolveLaterTypeReference;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.typedefinition.DefinitionName;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.ReferenceType;

final class TypeFinderStackEntry {
	private final BaseASTElement element;
	private final List<String> scope;
	private final NamespaceReference namespace;
	private final List<DefinitionName> outerTypes;
	private final boolean mayHaveNestedTypes;
	
	private List<CompiledType> nestedTypes;
	private List<CompiledTypeDependency> extendsFrom;
	private List<CompiledTypeDependency> dependencies;
	
	TypeFinderStackEntry(BaseASTElement element, List<String> scope, NamespaceReference namespace, List<DefinitionName> outerTypes, boolean mayHaveNestedTypes) {

		Objects.requireNonNull(element);
		
		this.element = element;
		this.scope = scope;
		this.namespace = namespace;
		this.outerTypes = outerTypes;
		this.mayHaveNestedTypes = mayHaveNestedTypes;
	}
	
	BaseASTElement getElement() {
		return element;
	}

	List<String> getScope() {
		return scope;
	}

	List<DefinitionName> getOuterTypes() {
		return outerTypes;
	}

	NamespaceReference getNamespace() {
		return namespace;
	}

	boolean mayHaveNestedTypes() {
		return mayHaveNestedTypes;
	}
	
	List<CompiledType> getNestedTypes() {
		return nestedTypes;
	}

	List<CompiledTypeDependency> getExtendsFrom() {
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
	
	void addExtendsFrom(ScopedName extendsFrom, TypeVariant typeVariant, ResolveLaterTypeReference element) {

		Objects.requireNonNull(extendsFrom);
		
		if (this.extendsFrom == null) {
			this.extendsFrom = new ArrayList<>();
		}
		
		this.extendsFrom.add(new ParsedTypeReference(extendsFrom, ReferenceType.EXTENDS_FROM, element, null));
	}
	
	void addDependency(ScopedName scopedName, ReferenceType type, ResolveLaterTypeReference element, BiConsumer<BaseType, TypeResolveMode> updateOnResolve) {

		final ParsedTypeReference dependency = new ParsedTypeReference(scopedName, type, element, updateOnResolve);
		
		Objects.requireNonNull(dependency);
		Objects.requireNonNull(type);
		
		if (this.dependencies == null) {
			this.dependencies = new ArrayList<>();
		}
		
		this.dependencies.add(dependency);
	}

	@Override
	public String toString() {
		return element.toString() + " extendsFrom=" + extendsFrom;
	}
}