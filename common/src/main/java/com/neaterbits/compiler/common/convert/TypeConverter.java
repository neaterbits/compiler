package com.neaterbits.compiler.common.convert;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;

public interface TypeConverter extends TypeVisitor<Context, TypeReference> {

}
