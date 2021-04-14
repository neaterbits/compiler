package com.neaterbits.compiler.convert;

import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.typereference.TypeReferenceVisitor;

public interface TypeReferenceConverter<T extends ConverterState<T>> extends TypeReferenceVisitor<T, TypeReference> {

	
}
