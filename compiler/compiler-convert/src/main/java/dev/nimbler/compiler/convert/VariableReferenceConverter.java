package dev.nimbler.compiler.convert;

import dev.nimbler.compiler.ast.objects.variables.VariableReference;
import dev.nimbler.compiler.ast.objects.variables.VariableReferenceVisitor;

public interface VariableReferenceConverter<T extends ConverterState<T>> extends VariableReferenceVisitor<T, VariableReference> {

}
