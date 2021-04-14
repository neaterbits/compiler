package dev.nimbler.compiler.convert;

import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.TypeVisitor;

public interface TypeConverter<T extends ConverterState<T>> extends TypeVisitor<T, BaseType> {

}
