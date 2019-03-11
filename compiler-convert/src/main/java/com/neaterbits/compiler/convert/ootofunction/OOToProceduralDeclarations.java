package com.neaterbits.compiler.convert.ootofunction;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.complex.StructType;

public class OOToProceduralDeclarations<T extends OOToProceduralClassDeclaration> {

	private final Map<ComplexType<?, ?, ?>, T> declarations;
	
	public OOToProceduralDeclarations() {
		this.declarations = new HashMap<>();
	}

	public OOToProceduralDeclarations(Map<ComplexType<?, ?, ?>, T> classToStruct) {
		
		Objects.requireNonNull(classToStruct);
		
		this.declarations = classToStruct;
	}
	
	public final StructType getClassStructType(ComplexType<?, ?, ?> type) {
		
		final T declaration = declarations.get(type);
		
		return declaration != null ? declaration.getDataFieldStructType() : null;
	}

	public final StructType getVTableStructType(ComplexType<?, ?, ?> type) {

		final T declaration = declarations.get(type);
		
		return declaration != null ? declaration.getVTableStructType() : null;
	}

	public final void add(T classDeclaration) {

		Objects.requireNonNull(classDeclaration);

		if (declarations.containsKey(classDeclaration.getClassType())) {
			throw new IllegalStateException();
		}

		declarations.put(classDeclaration.getClassType(), classDeclaration);
	}

	public Collection<T> getDeclarations() {
		return Collections.unmodifiableCollection(declarations.values());
	}
}