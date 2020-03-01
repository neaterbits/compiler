package com.neaterbits.compiler.main;

import com.neaterbits.compiler.ast.objects.type.BaseType;
import com.neaterbits.compiler.ast.objects.type.CompleteName;
import com.neaterbits.compiler.ast.objects.type.PointerType;
import com.neaterbits.compiler.ast.objects.type.TypeDefType;
import com.neaterbits.compiler.ast.objects.type.primitive.NamedVoidType;
import com.neaterbits.compiler.ast.objects.type.primitive.StringType;
import com.neaterbits.compiler.convert.ConverterState;
import com.neaterbits.compiler.convert.ootofunction.BaseTypeConverter;
import com.neaterbits.compiler.util.name.BaseTypeName;

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
