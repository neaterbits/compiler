package com.neaterbits.compiler.common.resolver.codemap;

import java.util.Arrays;

import static com.neaterbits.compiler.common.resolver.codemap.ArrayAllocation.allocateIntArray;

final class FileReferences<FILE> extends BaseCodeMap {

	private int fileSequenceNo;
	
	
	private int [] fileByType;
	private int [][] typesByFile;
	
	FileReferences() {
	}
	
	int addFile(int [] types) {

		final int fileNo = fileSequenceNo ++;
		
		this.typesByFile = allocateIntArray(this.typesByFile, fileNo + 1);
		
		if (typesByFile[fileNo] != null) {
			throw new IllegalStateException();
		}
		
		typesByFile[fileNo] = Arrays.copyOf(types, types.length);
		
		for (int type : types) {
			this.fileByType = allocateIntArray(fileByType, type + 1);
			
			this.fileByType[type] = fileNo;
		}
		
		return fileNo;
	}
}
