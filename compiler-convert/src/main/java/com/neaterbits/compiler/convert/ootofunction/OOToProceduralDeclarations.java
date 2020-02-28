package com.neaterbits.compiler.convert.ootofunction;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.ast.type.complex.StructType;
import com.neaterbits.compiler.util.TypeName;

public class OOToProceduralDeclarations<T extends OOToProceduralClassDeclaration> {

	private final Map<TypeName, T> declarations;
	
	public OOToProceduralDeclarations() {
		this.declarations = new HashMap<>();
	}

	public OOToProceduralDeclarations(Map<TypeName, T> classToStruct) {
		
		Objects.requireNonNull(classToStruct);
		
		this.declarations = classToStruct;
	}
	
	public final StructType getClassStructType(TypeName type) {
		
		Objects.requireNonNull(type);
		
		final T declaration = declarations.get(type);
		
		return declaration != null ? declaration.getDataFieldStructType() : null;
	}

	public final StructType getVTableStructType(TypeName type) {

		final T declaration = declarations.get(type);
		
		return declaration != null ? declaration.getVTableStructType() : null;
	}

	public final void add(T classDeclaration) {

		Objects.requireNonNull(classDeclaration);

		if (declarations.containsKey(classDeclaration.getClassType().getTypeName())) {
			throw new IllegalStateException();
		}

		declarations.put(classDeclaration.getTypeName(), classDeclaration);
	}

	public Collection<T> getDeclarations() {
		return Collections.unmodifiableCollection(declarations.values());
	}
}
