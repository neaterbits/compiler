package com.neaterbits.compiler.common.resolver.codemap;

import java.util.Objects;

import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.codemap.Encode.TypeTest;

import static com.neaterbits.compiler.common.resolver.codemap.Encode.decodeTypeNo;
import static com.neaterbits.compiler.common.resolver.codemap.Encode.encodeType;

import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.allocateIntArray;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.allocateArray;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.addToSubIntArray;
import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.subIntArrayCopy;

final class TypeHierarchy extends BaseCodeMap {

	private int typeSequenceNo;
	
	private TypeVariant [] typeVariant;
	
	private int [][] extendsFromEncoded;
	private int [][] extendedByEncoded;

	TypeHierarchy() {
	}
	
	int addType(TypeVariant typeVariant, int [] extendsFromEncoded) {

		Objects.requireNonNull(typeVariant);

		final int typeNo = typeSequenceNo ++;
		final int numTypes = typeSequenceNo;

		this.typeVariant		= allocateArray(this.typeVariant, numTypes, length -> new TypeVariant[length]);
		this.extendsFromEncoded = allocateIntArray(this.extendsFromEncoded, numTypes);
		this.extendedByEncoded  = allocateIntArray(this.extendedByEncoded,  numTypes);

		this.typeVariant[typeNo] = typeVariant;
		
		if (extendsFromEncoded != null && extendsFromEncoded.length != 0) {
			this.extendsFromEncoded[typeNo] = extendsFromEncoded;
			
			for (int extendedTypeNoEncoded : extendsFromEncoded) {
				setExtendedBy(decodeTypeNo(extendedTypeNoEncoded), typeNo, typeVariant);
			}
		}

		return typeNo;
	}
	
	TypeVariant getTypeVariantForType(int typeNo) {
		return typeVariant[typeNo];
	}
	
	private void setExtendedBy(int extendedTypeNo, int typeNo, TypeVariant typeVariant) {

		addToSubIntArray(this.extendedByEncoded, extendedTypeNo, encodeType(typeNo, typeVariant), 5);

	}
	
	int getExtendsFromSingleSuperClass(int typeNo) {
		
		final int [] extendsFromEncoded = this.extendsFromEncoded[typeNo];
		
		int found = -1;
		
		for (int extendsFrom : extendsFromEncoded) {
			if (Encode.isClass(extendsFrom)) {
				if (found != -1) {
					throw new IllegalStateException();
				}
				
				found = extendsFrom;
			}
		}
		
		return found;
	}
	
	int getNumExtendedBy(int typeNo) {
		final int [] extendedBy = extendedByEncoded[typeNo];

		return extendedBy != null ? extendedBy[0] : 0;
	}
	
	int getExtendedByTypeNoEncoded(int typeNo, int idx) {
		return extendedByEncoded[typeNo][idx + 1];
	}
	
	int [] getExtendedByTypeEncoded(int typeNo) {
		final int [] extendedBy = extendedByEncoded[typeNo];

		return extendedBy != null ? subIntArrayCopy(extendedBy) : null;
	}
	
	int [] getExtendsFrom(int typeNo, TypeTest typeTest) {

		final int [] extendsFromEncoded = this.extendsFromEncoded[typeNo];

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

		return resultTypes;
	}
}
