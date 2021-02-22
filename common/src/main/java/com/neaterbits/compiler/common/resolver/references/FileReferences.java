package com.neaterbits.compiler.common.resolver.references;

import java.util.ArrayList;
import java.util.List;

final class FileReferences<FILE> extends BaseReferences {

	private final List<FILE> files;
	private int [][] types;
	
	FileReferences() {
		this.files = new ArrayList<>();
	}
	
	int addFile(FILE file) {
		
		final int fileNo = files.size();
		
		files.add(file);
		
		this.types = allocateIntArray(types, fileNo + 1);
		
		return fileNo;
	}
	
	void setTypes(int fileNo, int [] types) {
		
		if (this.types[fileNo] != null) {
			throw new IllegalStateException("Types already set");
		}
		
		this.types[fileNo] = types;
	}
}
