package com.neaterbits.compiler.codemap.compiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.codemap.ArrayAllocation;
import com.neaterbits.compiler.codemap.IntCodeMap;
import com.neaterbits.compiler.codemap.MethodOverrideMap;

public class IntCompilerCodeMap extends IntCodeMap implements CompilerCodeMap {

	static final int SOURCEFILE_UNDEF = 0;
	
	private int sourceFileNo; // source file allocator
	private String [] sourceFiles;
	private final Map<String, Integer> sourceFileToIndex;

	private final FileReferences fileReferences;
	private final TokenCrossReference crossReference;
	
	public IntCompilerCodeMap(MethodOverrideMap methodOverrideMap) {
		super(methodOverrideMap);
		
		this.sourceFileToIndex = new HashMap<>();
		this.sourceFileNo = SOURCEFILE_UNDEF + 1;

		this.fileReferences = new FileReferences();
		this.crossReference = new TokenCrossReference();
	}

	@Override
	public int addFile(String file, int[] types) {

		Objects.requireNonNull(file);
		
		if (!file.trim().equals(file)) {
			throw new IllegalArgumentException();
		}
		
		if (file.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		if (sourceFileToIndex.containsKey(file)) {
			throw new IllegalArgumentException();
		}
		
		final int sourceFileIdx = sourceFileNo ++;
	
		this.sourceFiles = ArrayAllocation.allocateArray(sourceFiles, ArrayAllocation.DEFAULT_LENGTH, String[]::new);
		
		sourceFiles[sourceFileIdx] = file;
		sourceFileToIndex.put(file, sourceFileIdx);
		
		fileReferences.addFile(sourceFileIdx, types);
		
		return sourceFileIdx;
	}
	
	@Override
	public void removeFile(String file) {
		
		Objects.requireNonNull(file);

		final Integer sourceFileIdx = sourceFileToIndex.remove(file);
		
		if (sourceFileIdx == null) {
			throw new IllegalStateException();
		}
		
		sourceFiles[sourceFileIdx] = null;
		crossReference.removeFile(sourceFileIdx);
	}

	@Override
	public int addToken(int sourceFile, int tokenOffset, int tokenLength) {
		
		if (sourceFiles[sourceFile] == null) {
			throw new IllegalArgumentException();
		}

		return crossReference.addToken(sourceFile, tokenOffset, tokenLength);
	}
}
