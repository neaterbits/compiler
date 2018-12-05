package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.PointerType;
import com.neaterbits.compiler.common.ast.type.TypeDefType;
import com.neaterbits.compiler.common.ast.type.BaseTypeName;
import com.neaterbits.compiler.common.ast.type.primitive.NamedVoidType;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;
import com.neaterbits.compiler.common.convert.ConverterState;
import com.neaterbits.compiler.common.convert.ootofunction.BaseTypeConverter;

public abstract class BaseJavaToCTypeConverter<T extends ConverterState<T>>
	extends BaseTypeConverter<T> {

	private static final NamedVoidType VOID_TYPE = new NamedVoidType(new BaseTypeName("void"));
	
	private static final TypeDefType STRING_TYPE = new TypeDefType(
			new CompleteName(null, null, new BaseTypeName("String")),
			new PointerType(VOID_TYPE, 1));
	
	@Override
	public final BaseType onString(StringType type, T param) {
		return STRING_TYPE;
	}
}
