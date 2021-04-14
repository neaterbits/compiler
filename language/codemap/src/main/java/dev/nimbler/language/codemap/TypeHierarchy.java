package dev.nimbler.language.codemap;

import static dev.nimbler.language.codemap.ArrayAllocation.addToSubIntArray;
import static dev.nimbler.language.codemap.ArrayAllocation.allocateArray;
import static dev.nimbler.language.codemap.ArrayAllocation.allocateIntArray;
import static dev.nimbler.language.codemap.ArrayAllocation.subIntArrayCopy;
import static dev.nimbler.language.codemap.Encode.decodeTypeNo;
import static dev.nimbler.language.codemap.Encode.encodeType;

import java.util.Objects;

import dev.nimbler.language.codemap.Encode.TypeTest;
import dev.nimbler.language.common.types.TypeVariant;

final class TypeHierarchy {

	private int typeSequenceNo;

	private TypeVariant [] typeVariant;

	private int [][] thisExtendsFromEncoded;
	private int [][] extendingFromThisEncoded;

	int addType(TypeVariant typeVariant, int [] thisExtendsFromClassesEncoded, int [] thisExtendsFromInterfacesEncoded) {

		Objects.requireNonNull(typeVariant);

		final int typeNo = typeSequenceNo ++;
		final int numTypes = typeSequenceNo;

		this.typeVariant		= allocateArray(this.typeVariant, numTypes, length -> new TypeVariant[length]);
		this.thisExtendsFromEncoded = allocateIntArray(this.thisExtendsFromEncoded, numTypes);
		this.extendingFromThisEncoded  = allocateIntArray(this.extendingFromThisEncoded,  numTypes);

		this.typeVariant[typeNo] = typeVariant;

		if (    thisExtendsFromClassesEncoded != null && thisExtendsFromClassesEncoded.length != 0
		     && thisExtendsFromInterfacesEncoded != null && thisExtendsFromInterfacesEncoded.length != 0) {


			final int extendsFromLength = thisExtendsFromClassesEncoded.length + thisExtendsFromInterfacesEncoded.length;

			this.thisExtendsFromEncoded[typeNo] = new int[extendsFromLength];

			for (int i = 0; i < thisExtendsFromClassesEncoded.length; ++ i) {
				this.thisExtendsFromEncoded[typeNo][i] = thisExtendsFromClassesEncoded[i];
			}

			for (int i = 0; i < thisExtendsFromInterfacesEncoded.length; ++ i) {
				this.thisExtendsFromEncoded[typeNo][i + thisExtendsFromClassesEncoded.length]
							= thisExtendsFromInterfacesEncoded[i];
			}

			setExtendedBy(typeNo, typeVariant, thisExtendsFromClassesEncoded);
            setExtendedBy(typeNo, typeVariant, thisExtendsFromInterfacesEncoded);
		}
		else if (thisExtendsFromClassesEncoded != null && thisExtendsFromClassesEncoded.length != 0) {

			this.thisExtendsFromEncoded[typeNo] = thisExtendsFromClassesEncoded;

			setExtendedBy(typeNo, typeVariant, thisExtendsFromClassesEncoded);
		}
		else if (thisExtendsFromInterfacesEncoded != null && thisExtendsFromInterfacesEncoded.length != 0) {

			this.thisExtendsFromEncoded[typeNo] = thisExtendsFromInterfacesEncoded;

            setExtendedBy(typeNo, typeVariant, thisExtendsFromInterfacesEncoded);
		}
		else {
			this.thisExtendsFromEncoded[typeNo] = null;
		}

		/*
		System.out.println("## addType " + typeNo + " extends from " + Arrays.toString(decodeTypes(this.thisExtendsFromEncoded[typeNo]))
				+ ", classes " + Arrays.toString(decodeTypes(thisExtendsFromClassesEncoded))
				+ ", interfaces " + Arrays.toString(decodeTypes(thisExtendsFromInterfacesEncoded)));
		*/
		return typeNo;
	}

	/*
	private static int [] decodeTypes(int [] encodedTypes) {

		final int [] result;

		if (encodedTypes == null) {
			result = null;
		}
		else {
			result = new int[encodedTypes.length];

			for (int i = 0; i < encodedTypes.length; ++ i) {
				result[i] = Encode.decodeTypeNo(encodedTypes[i]);
			}
		}

		return result;
	}
	*/

	private void setExtendedBy(int typeNo, TypeVariant typeVariant, int [] thisExtendsFromEncoded) {

	    for (int extendedTypeNoEncoded : thisExtendsFromEncoded) {
			setExtendedBy(decodeTypeNo(extendedTypeNoEncoded), typeNo, typeVariant);
		}
	}

	TypeVariant getTypeVariantForType(int typeNo) {
		return typeVariant[typeNo];
	}

	private void setExtendedBy(int extendedTypeNo, int typeNo, TypeVariant typeVariant) {

		addToSubIntArray(this.extendingFromThisEncoded, extendedTypeNo, encodeType(typeNo, typeVariant), 5);

	}

	int getExtendsFromSingleSuperClass(int typeNo) {
		final int encoded = getExtendsFromSingleSuperClassEncoded(typeNo);

		return encoded != -1 ? Encode.decodeTypeNo(encoded) : -1;
	}

	private int getExtendsFromSingleSuperClassEncoded(int typeNo) {

	    if (typeVariant[typeNo] != TypeVariant.CLASS) {
	        throw new IllegalArgumentException("Expected class");
	    }

		final int [] extendsFromEncodedArray = this.thisExtendsFromEncoded[typeNo];

		int found = -1;

		if (extendsFromEncodedArray != null) {
			for (int extendsFromEncoded : extendsFromEncodedArray) {
				if (Encode.isClass(extendsFromEncoded)) {
					if (found != -1) {
						throw new IllegalStateException();
					}

					found = extendsFromEncoded;
				}
			}
		}

		return found;
	}

	int [] getTypesThisExtendsFromEncoded(int typeNo) {
		return thisExtendsFromEncoded[typeNo];
	}

	int getNumExtendingThis(int typeNo) {
		final int [] extendedBy = extendingFromThisEncoded[typeNo];

		return extendedBy != null ? extendedBy[0] : 0;
	}

	int [] getTypesExtendingThisEncoded(int typeNo) {

		final int [] extendingFromThis = extendingFromThisEncoded[typeNo];

		return extendingFromThis != null ? subIntArrayCopy(extendingFromThis) : null;
	}

	int [] getTypesThisExtendsFrom(int typeNo, TypeTest typeTest) {

		final int [] extendsFromEncoded = this.thisExtendsFromEncoded[typeNo];

		final int [] resultTypes;

		if (extendsFromEncoded != null) {

			int numEntries;

			if (typeTest == null) {
				numEntries = extendsFromEncoded.length;
			}
			else {
				numEntries = 0;

				for (int i = 0; i < extendsFromEncoded.length; ++ i) {
					if (typeTest.onTypeNoEncoded(extendsFromEncoded[i])) {
						++ numEntries;
					}
				}
			}

			if (numEntries > 0) {

    			resultTypes = new int[numEntries];

    			int dstIdx = 0;

    			for (int i = 0; i < extendsFromEncoded.length; ++ i) {
    				if (typeTest == null || typeTest.onTypeNoEncoded(extendsFromEncoded[i])) {
    					resultTypes[dstIdx ++] = decodeTypeNo(extendsFromEncoded[i]);
    				}
    			}
			}
			else {
			    resultTypes = null;
			}
		}
		else {
			resultTypes = null;
		}

		return resultTypes;
	}

	int [] encodeTypeVariant(int [] types) {
		final int [] result = new int[types.length];

		for (int i = 0; i < result.length; ++ i) {
			final int extendsFromType = types[i];

			result[i] = Encode.encodeType(
					extendsFromType,
					getTypeVariantForType(extendsFromType));
		}

		return result;
	}
}
