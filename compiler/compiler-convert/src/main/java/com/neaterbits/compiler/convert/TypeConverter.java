package com.neaterbits.compiler.convert;

import com.neaterbits.compiler.ast.objects.type.BaseType;
import com.neaterbits.compiler.ast.objects.type.TypeVisitor;

public interface TypeConverter<T extends ConverterState<T>> extends TypeVisitor<T, BaseType> {

}
