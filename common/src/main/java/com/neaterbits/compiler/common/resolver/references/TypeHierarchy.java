package com.neaterbits.compiler.common.resolver.references;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.TypeVariant;

final class TypeHierarchy<TYPE extends ResolvedType> extends BaseReferences {

	private final List<TYPE> types;
	
	private int [] fileNo;
	
	private int [][] extendsFromEncoded;
	private int [][] extendedByEncoded;

	TypeHierarchy() {
		this.types = new ArrayList<>();
	}
	
	int addType(int fileNo, TYPE type, int [] extendsFromEncoded) {

		Objects.requireNonNull(type);
		
		int typeNo = types.size();
		
		types.add(type);

		final int numTypes = types.size();

		this.fileNo 	 		= allocateIntArray(this.fileNo, 	  		numTypes);
		this.extendsFromEncoded = allocateIntArray(this.extendsFromEncoded, numTypes);
		this.extendedByEncoded  = allocateIntArray(this.extendedByEncoded,  numTypes);

		this.fileNo[typeNo] = fileNo;
		
		if (extendsFromEncoded != null && extendsFromEncoded.length != 0) {
			this.extendsFromEncoded[typeNo] = extendsFromEncoded;
			
			for (int extendedTypeNoEncoded : extendsFromEncoded) {
				setExtendedBy(decodeTypeNo(extendedTypeNoEncoded), typeNo, type.getTypeVariant());
			}
		}

		return typeNo;
	}
	
	private void setExtendedBy(int extendedTypeNo, int typeNo, TypeVariant typeVariant) {
		int [] extendedBy = this.extendedByEncoded[extendedTypeNo];
		
		if (extendedBy == null) {
			extendedBy = this.extendedByEncoded[extendedTypeNo] = new int[5];
		}

		final int numEntries = extendedBy[0];

		if (numEntries + 1 + 1> extendedBy.length) {
			this.extendedByEncoded[typeNo] = extendedBy = Arrays.copyOf(extendedBy, extendedBy.length * 2);
		}

		extendedBy[numEntries + 1] = encodeType(typeNo, typeVariant);

		++ extendedBy[0];
	}
	
	TYPE getType(int typeNo) {
		return types.get(typeNo);
	}

	int [] getExtendedFromEncoded(int typeNo) {
		return extendsFromEncoded[typeNo];
	}
	
	int getNumExtendedBy(int typeNo) {
		final int [] extendedBy = extendedByEncoded[typeNo];

		return extendedBy != null ? extendedBy[0] : 0;
	}
	
	int getExtendedByTypeNoEncoded(int typeNo, int idx) {
		return extendedByEncoded[typeNo][idx + 1];
	}
	
	List<TYPE> getExtendsFrom(int typeNo, TypeTest typeTest) {
		final List<TYPE> resultTypes = new ArrayList<>();

		final int [] extendsFrom = this.extendsFromEncoded[typeNo];

		if (extendsFrom != null) {
			for (int typeNoEncoded : extendsFrom) {
				if (typeTest.onTypeNoEncoded(typeNoEncoded)) {
					resultTypes.add(types.get(decodeTypeNo(typeNoEncoded)));
				}
			}
		}
		
		return resultTypes;
	}
}
