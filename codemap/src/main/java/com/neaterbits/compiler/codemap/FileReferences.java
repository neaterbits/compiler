package com.neaterbits.compiler.codemap;

import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateIntArray;

import java.util.Arrays;

public final class FileReferences<FILE> {

	private int fileSequenceNo;
	
	private int [] fileByType;
	private int [][] typesByFile;
	
	public int addFile(int [] types) {

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
