package com.neaterbits.compiler.convert;

import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.ast.variables.VariableReferenceVisitor;

public interface VariableReferenceConverter<T extends ConverterState<T>> extends VariableReferenceVisitor<T, VariableReference> {

}
