package com.neaterbits.compiler.convert;

import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.TypeVisitor;

public interface TypeConverter<T extends ConverterState<T>> extends TypeVisitor<T, BaseType> {

}
