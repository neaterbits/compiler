package com.neaterbits.compiler.convert;

import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReferenceVisitor;

public interface TypeReferenceConverter<T extends ConverterState<T>> extends TypeReferenceVisitor<T, TypeReference> {

	
}
