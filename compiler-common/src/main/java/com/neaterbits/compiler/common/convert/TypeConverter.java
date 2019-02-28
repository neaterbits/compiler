package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public interface TypeConverter<T extends ConverterState<T>> extends TypeVisitor<T, BaseType> {

}
