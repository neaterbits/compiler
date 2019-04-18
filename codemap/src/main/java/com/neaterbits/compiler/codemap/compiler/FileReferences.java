package com.neaterbits.compiler.codemap.compiler;

import static com.neaterbits.compiler.codemap.ArrayAllocation.allocateIntArray;

import java.util.Arrays;

public final class FileReferences {

	private int [] fileByType;
	private int [][] typesByFile;
	
	public int addFile(int fileNo, int [] types) {

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

	void removeFile(int fileNo) {

		for (int type : typesByFile[fileNo]) {
			fileByType[type] = IntCompilerCodeMap.SOURCEFILE_UNDEF;
		}
		
		typesByFile[fileNo] = null;
	}
}
