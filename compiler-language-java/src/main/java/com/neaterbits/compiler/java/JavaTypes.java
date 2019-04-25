package com.neaterbits.compiler.java;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.compiler.ast.type.BaseTypeName;
import com.neaterbits.compiler.ast.type.primitive.BooleanType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.type.primitive.ByteType;
import com.neaterbits.compiler.ast.type.primitive.Char16Type;
import com.neaterbits.compiler.ast.type.primitive.DoubleType;
import com.neaterbits.compiler.ast.type.primitive.FloatType;
import com.neaterbits.compiler.ast.type.primitive.IntType;
import com.neaterbits.compiler.ast.type.primitive.LongType;
import com.neaterbits.compiler.ast.type.primitive.NamedVoidType;
import com.neaterbits.compiler.ast.type.primitive.ScalarType;
import com.neaterbits.compiler.ast.type.primitive.ShortType;
import com.neaterbits.compiler.ast.type.primitive.StringType;
import com.neaterbits.compiler.util.imports.TypeImport;
import com.neaterbits.compiler.util.model.BuiltinTypeRef;

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
