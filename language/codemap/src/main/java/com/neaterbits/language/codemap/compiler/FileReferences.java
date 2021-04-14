package com.neaterbits.language.codemap.compiler;

import static com.neaterbits.language.codemap.ArrayAllocation.allocateIntArray;

import java.util.Arrays;

public final class FileReferences {

	private static final int [] EMPTY_ARRAY = new int[0];

	private int [] fileByType;
	private int [][] typesByFile;

	int addFile(int fileNo, int [] types) {

		this.typesByFile = allocateIntArray(this.typesByFile, fileNo + 1);

		if (typesByFile[fileNo] != null) {
			throw new IllegalStateException();
		}

		if (types != null) {
			typesByFile[fileNo] = Arrays.copyOf(types, types.length);

			for (int type : types) {
				this.fileByType = allocateIntArray(fileByType, type + 1);

				this.fileByType[type] = fileNo;
			}
		}
		else {
			typesByFile[fileNo] = EMPTY_ARRAY;
		}

		return fileNo;
	}

	int [] getTypesForFile(int fileNo) {

	    if (fileNo >= typesByFile.length) {
	        throw new IllegalArgumentException();
	    }

	    return typesByFile[fileNo];
	}

	void removeFile(int fileNo) {

		for (int type : typesByFile[fileNo]) {
			fileByType[type] = -1;
		}

		typesByFile[fileNo] = null;
	}
}
