package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.ast.variables.VariableReferenceVisitor;

public interface VariableReferenceConverter<T extends ConverterState<T>> extends VariableReferenceVisitor<T, VariableReference> {

}
