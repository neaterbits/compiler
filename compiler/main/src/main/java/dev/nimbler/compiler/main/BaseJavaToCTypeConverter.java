package dev.nimbler.compiler.main;

import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.PointerType;
import dev.nimbler.compiler.ast.objects.type.TypeDefType;
import dev.nimbler.compiler.ast.objects.type.primitive.NamedVoidType;
import dev.nimbler.compiler.ast.objects.type.primitive.StringType;
import dev.nimbler.compiler.convert.ConverterState;
import dev.nimbler.compiler.convert.ootofunction.BaseTypeConverter;
import dev.nimbler.compiler.util.name.BaseTypeName;

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
