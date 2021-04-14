package com.neaterbits.compiler.convert;

import com.neaterbits.compiler.ast.objects.variables.VariableReference;
import com.neaterbits.compiler.ast.objects.variables.VariableReferenceVisitor;

public interface VariableReferenceConverter<T extends ConverterState<T>> extends VariableReferenceVisitor<T, VariableReference> {

}
