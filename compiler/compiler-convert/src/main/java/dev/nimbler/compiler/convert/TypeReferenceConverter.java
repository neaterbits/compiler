package dev.nimbler.compiler.convert;

import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReferenceVisitor;

public interface TypeReferenceConverter<T extends ConverterState<T>> extends TypeReferenceVisitor<T, TypeReference> {

	
}
