package com.neaterbits.compiler.java;

import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.primitive.BooleanType;
import com.neaterbits.compiler.common.ast.type.primitive.ByteType;
import com.neaterbits.compiler.common.ast.type.primitive.Char16Type;
import com.neaterbits.compiler.common.ast.type.primitive.DoubleType;
import com.neaterbits.compiler.common.ast.type.primitive.FloatType;
import com.neaterbits.compiler.common.ast.type.primitive.IntType;
import com.neaterbits.compiler.common.ast.type.primitive.LongType;
import com.neaterbits.compiler.common.ast.type.primitive.NamedVoidType;
import com.neaterbits.compiler.common.ast.type.primitive.ShortType;
import com.neaterbits.compiler.common.ast.type.primitive.StringType;

public class JavaTypes {

	public static final ByteType 	BYTE_TYPE 	= new ByteType	(new TypeName("byte"), false);
	public static final ShortType 	SHORT_TYPE 	= new ShortType	(new TypeName("short"), false);
	public static final IntType 	INT_TYPE 	= new IntType	(new TypeName("int"), false);
	public static final LongType 	LONG_TYPE 	= new LongType	(new TypeName("long"), false);
	public static final Char16Type  CHAR_TYPE 	= new Char16Type(new TypeName("char"), false);
	public static final FloatType 	FLOAT_TYPE 	= new FloatType	(new TypeName("float"), false);
	public static final DoubleType  DOUBLE_TYPE = new DoubleType(new TypeName("double"), false);
	public static final BooleanType BOOLEAN_TYPE = new BooleanType(new TypeName("boolean"), false);
	public static final StringType  STRING_TYPE = new StringType(new TypeName("String"), true);
	public static final NamedVoidType VOID_TYPE = new NamedVoidType(new TypeName("void"));

}
