package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.TypeReferenceVisitor;

public interface TypeReferenceConverter<T extends ConverterState<T>> extends TypeReferenceVisitor<T, TypeReference> {

	
}
