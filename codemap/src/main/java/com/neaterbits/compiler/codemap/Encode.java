package com.neaterbits.compiler.codemap;

import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.types.Mutability;
import com.neaterbits.compiler.types.Visibility;
import com.neaterbits.compiler.util.Bits;

class Encode {
	private static final int TYPEVARIANT_BITS = 2;
	private static final int TYPEVARIANT_MASK = ((1 << TYPEVARIANT_BITS) - 1) << (32 - TYPEVARIANT_BITS);

	static final int TYPE_BITS = IdentifierBits.TYPE_BITS;
	static final int SIGNATURE_BITS = 20;
	static final int METHOD_VARIANT_BITS = 2;
	static final int METHOD_BITS = IdentifierBits.METHOD_BITS;

	static final int FIELD_BITS = IdentifierBits.FIELD_BITS;
	static final int FIELD_MASK = Bits.intMask(FIELD_BITS, 0);

	private static final int FIELD_VISIBILITY_BITS = Bits.getNumBitsForStoringEnum(Visibility.class);
	private static final int FIELD_VISIBILITY_SHIFT = FIELD_BITS;
	private static final int FIELD_VISIBILITY_MASK = Bits.intMask(FIELD_VISIBILITY_BITS, FIELD_VISIBILITY_SHIFT);

	private static final int FIELD_MUTABILITY_BITS = Bits.getNumBitsForStoringEnum(Mutability.class);
	private static final int FIELD_MUTABILITY_SHIFT = FIELD_VISIBILITY_SHIFT + FIELD_VISIBILITY_BITS;
	private static final int FIELD_MUTABILITY_MASK = Bits.intMask(FIELD_MUTABILITY_BITS, FIELD_MUTABILITY_SHIFT);

	private static final int FIELD_STATIC_BITS = 1;
	private static final int FIELD_STATIC_SHIFT = FIELD_MUTABILITY_SHIFT + FIELD_MUTABILITY_BITS;

	private static final int FIELD_VOLATILE_BITS = 1;
	private static final int FIELD_VOLATILE_SHIFT = FIELD_STATIC_SHIFT + FIELD_STATIC_BITS;

	private static final int FIELD_TRANSIENT_BITS = 1;
	private static final int FIELD_TRANSIENT_SHIFT = FIELD_VOLATILE_SHIFT + FIELD_VOLATILE_BITS;

	static final int METHOD_AND_VARIANT_BITS = METHOD_BITS + METHOD_VARIANT_BITS;

	static final int MAX_TYPES 		= 1 << TYPE_BITS;
	static final int MAX_SIGNATURES = 1 << SIGNATURE_BITS;
	static final int MAX_METHODS 	= 1 << METHOD_BITS;

	static final int METHOD_MASK = (1 << METHOD_BITS) - 1;

	static final int PARAM_METHOD_NAME_BITS  = 20;
	static final int PARAM_TYPES_BITS = 20;
	static final int SIGNATURENO_BITS = 24;

	static final int MAX_SIGNATURENO = (1 << SIGNATURENO_BITS);

	@FunctionalInterface
	interface TypeTest {
		boolean onTypeNoEncoded(int typeNoEncoded);
	}

	static {
		if (TypeVariant.values().length > (1 << TYPEVARIANT_BITS)) {
			throw new IllegalStateException("More bits required for type variant");
		}

		if (
				FIELD_BITS
			  + FIELD_VISIBILITY_BITS
			  + FIELD_MUTABILITY_BITS
			  + FIELD_STATIC_BITS
			  + FIELD_VOLATILE_BITS
			  + FIELD_TRANSIENT_BITS > 32) {
			throw new IllegalStateException("More bits required for fields");
		}
	}

	static int encodeTypeVariant(int index, TypeVariant typeVariant) {
		return typeVariant.ordinal() << 30 | index;
	}

	static int encodeType(int typeNo, TypeVariant typeVariant) {
		return encodeTypeVariant(typeNo, typeVariant);
	}

	static TypeVariant getTypeVariant(int encodedTypeNo) {
		return TypeVariant.values()[encodedTypeNo >>> 30];
	}

	static boolean isInterface(int encodedTypeNo) {
		return getTypeVariant(encodedTypeNo) == TypeVariant.INTERFACE;
	}

	static boolean isClass(int encodedTypeNo) {
		return getTypeVariant(encodedTypeNo) == TypeVariant.CLASS;
	}

	static boolean isEnum(int encodedTypeNo) {
		return getTypeVariant(encodedTypeNo) == TypeVariant.ENUM;
	}

	private static int decodeIndex(int encodedIndex) {
		return encodedIndex & (~TYPEVARIANT_MASK);
	}

	static int decodeTypeNo(int encodedTypeNo) {
		return decodeIndex(encodedTypeNo);
	}

	static int encodeField(
			int fieldIndex,
			boolean isStatic,
			Visibility visibility,
			Mutability mutability,
			boolean isVolatile,
			boolean isTransient) {

		return    fieldIndex
				| (isStatic ? 1 : 0) << FIELD_STATIC_SHIFT
				| visibility.ordinal() << FIELD_VISIBILITY_SHIFT
				| mutability.ordinal() << FIELD_MUTABILITY_SHIFT
				| (isVolatile ? 1 : 0) << FIELD_VOLATILE_SHIFT
				| (isTransient ? 1 : 0) << FIELD_TRANSIENT_SHIFT;
	}

	static int decodeFieldNo(int encoded) {
		return encoded & FIELD_MASK;
	}

	static boolean isFieldStatic(int encoded) {
		return (encoded & (1 << FIELD_STATIC_SHIFT)) != 0;
	}

	static boolean isFieldVolatile(int encoded) {
		return (encoded & (1 << FIELD_VOLATILE_SHIFT)) != 0;
	}

	static boolean isFieldTransient(int encoded) {
		return (encoded & (1 << FIELD_TRANSIENT_SHIFT)) != 0;
	}

	static Visibility getFieldVisibility(int encoded) {
		return Visibility.values()[(encoded & FIELD_VISIBILITY_MASK) >> FIELD_VISIBILITY_SHIFT];
	}

	static Mutability getFieldMutability(int encoded) {
		return Mutability.values()[(encoded & FIELD_MUTABILITY_MASK) >> FIELD_MUTABILITY_SHIFT];
	}

	static int encodeMethod(int methodNo, TypeVariant typeVariant, MethodVariant methodVariant) {
		return encodeTypeVariant(methodVariant.ordinal() << METHOD_BITS | methodNo , typeVariant);
	}

	static int encodeMethodWithMethodVariant(int methodNoWihMethodVariant, TypeVariant typeVariant) {

		return encodeTypeVariant(methodNoWihMethodVariant, typeVariant);

	}

	static int encodeMethodWithoutTypeVariant(int methodNo, MethodVariant methodVariant) {
		return methodVariant.ordinal() << METHOD_BITS | methodNo;
	}

	static int decodeMethodNo(int encodedMethodNo) {
		return encodedMethodNo & METHOD_MASK;
	}

	static MethodVariant getMethodVariant(int encodedMethodNo) {
		return MethodVariant.values()[(encodedMethodNo >> METHOD_BITS) & ((1 << METHOD_VARIANT_BITS) - 1)];
	}
}
