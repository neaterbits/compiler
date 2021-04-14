package dev.nimbler.compiler.language.java;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dev.nimbler.compiler.ast.objects.type.primitive.BooleanType;
import dev.nimbler.compiler.ast.objects.type.primitive.BuiltinType;
import dev.nimbler.compiler.ast.objects.type.primitive.ByteType;
import dev.nimbler.compiler.ast.objects.type.primitive.Char16Type;
import dev.nimbler.compiler.ast.objects.type.primitive.DoubleType;
import dev.nimbler.compiler.ast.objects.type.primitive.FloatType;
import dev.nimbler.compiler.ast.objects.type.primitive.IntType;
import dev.nimbler.compiler.ast.objects.type.primitive.LongType;
import dev.nimbler.compiler.ast.objects.type.primitive.NamedVoidType;
import dev.nimbler.compiler.ast.objects.type.primitive.ScalarType;
import dev.nimbler.compiler.ast.objects.type.primitive.ShortType;
import dev.nimbler.compiler.ast.objects.type.primitive.StringType;
import dev.nimbler.compiler.model.common.BuiltinTypeRef;
import dev.nimbler.compiler.types.imports.TypeImport;
import dev.nimbler.compiler.util.name.BaseTypeName;

public class JavaTypes {

	public static final ByteType 	BYTE_TYPE 	= new ByteType	(new BaseTypeName("byte"), false);
	public static final ShortType 	SHORT_TYPE 	= new ShortType	(new BaseTypeName("short"), false);
	public static final IntType 	INT_TYPE 	= new IntType	(new BaseTypeName("int"), false);
	public static final LongType 	LONG_TYPE 	= new LongType	(new BaseTypeName("long"), false);
	public static final Char16Type  CHAR_TYPE 	= new Char16Type(new BaseTypeName("char"), false);
	public static final FloatType 	FLOAT_TYPE 	= new FloatType	(new BaseTypeName("float"), false);
	public static final DoubleType  DOUBLE_TYPE = new DoubleType(new BaseTypeName("double"), false);
	public static final BooleanType BOOLEAN_TYPE = new BooleanType(new BaseTypeName("boolean"), false);
	public static final StringType  STRING_TYPE = new StringType(new BaseTypeName("String"), true);
	public static final NamedVoidType VOID_TYPE = new NamedVoidType(new BaseTypeName("void"));

	private static final Collection<BuiltinType> builtinTypes = Collections.unmodifiableList(Arrays.asList(
			BYTE_TYPE,
			SHORT_TYPE,
			INT_TYPE,
			LONG_TYPE,
			CHAR_TYPE,
			FLOAT_TYPE,
			DOUBLE_TYPE,
			BOOLEAN_TYPE,
			STRING_TYPE,
			VOID_TYPE
		));
	
	public static Collection<BuiltinType> getBuiltinTypes() {
		return builtinTypes;
	}
	
	public static Collection<BuiltinTypeRef> getBuiltinTypeRefs() {

		final Collection<BuiltinTypeRef> builtinTypeRefs = getBuiltinTypes().stream()
				.map(builtinType -> new BuiltinTypeRef(builtinType.getTypeName(), builtinType instanceof ScalarType))
				.collect(Collectors.toList());

		return builtinTypeRefs;
	}

	private static final List<TypeImport> IMPLICIT_IMPORTS = Collections.unmodifiableList(Arrays.asList(
			new TypeImport(new String [] { "java", "lang" })
	));
	
	public static List<TypeImport> getImplicitImports() {
		return IMPLICIT_IMPORTS;
	}
}
