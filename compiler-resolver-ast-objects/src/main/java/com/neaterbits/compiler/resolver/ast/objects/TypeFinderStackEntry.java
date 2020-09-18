package com.neaterbits.compiler.resolver.ast.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.name.DefinitionName;
import com.neaterbits.compiler.util.name.NamespaceReference;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.typereference.UnresolvedTypeReference;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.model.common.UpdateOnResolve;
import com.neaterbits.compiler.model.common.util.ReferenceLocation;

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

	void addNestedType(CompiledType nestedType) {
		Objects.requireNonNull(nestedType);
		
		if (this.nestedTypes == null) {
			this.nestedTypes = new ArrayList<>();
		}
		
		this.nestedTypes.add(nestedType);
	}
	
	void addExtendsFrom(CompilationUnit compilationUnit, ScopedName extendsFrom, TypeVariant typeVariant, UnresolvedTypeReference element) {

		Objects.requireNonNull(extendsFrom);
		
		if (this.extendsFrom == null) {
			this.extendsFrom = new ArrayList<>();
		}
		
		final int elementRef = compilationUnit.getParseTreeRefFromElement(element);
		
		this.extendsFrom.add(new CompiledTypeDependency(extendsFrom, ReferenceLocation.EXTENDS_FROM, elementRef, null, null));
	}
	
	void addDependency(
			CompilationUnit compilationUnit,
			ScopedName scopedName,
			ReferenceLocation type,
			UnresolvedTypeReference element,
			UpdateOnResolve updateOnResolve,
			BaseASTElement updateOnResolveElement) {

		final int elementRef = compilationUnit.getParseTreeRefFromElement(element);
		
		final Integer updateOnResolveElementRef = updateOnResolveElement != null
				? compilationUnit.getParseTreeRefFromElement(updateOnResolveElement)
				: null;
		
		final CompiledTypeDependency dependency = new CompiledTypeDependency(
				scopedName,
				type,
				elementRef, 
				updateOnResolve, 
				updateOnResolveElementRef);
		
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
