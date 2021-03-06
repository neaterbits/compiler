package dev.nimbler.language.codemap.compiler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import dev.nimbler.language.codemap.ArrayAllocation;
import dev.nimbler.language.codemap.IntCodeMap;
import dev.nimbler.language.codemap.NameToTypeNoMap;
import dev.nimbler.language.codemap.StaticMethodOverrideMap;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.TypesMap;

public class IntCompilerCodeMap extends IntCodeMap implements CompilerCodeMap {

	private int sourceFileNo; // source file allocator
	private String [] sourceFiles;
	private final Map<String, Integer> sourceFileToIndex;

	private final FileReferences fileReferences;
	private final TokenCrossReference crossReference;

	private final NameToTypeNoMap nameToTypeNoMap;

	public IntCompilerCodeMap() {
		super(new StaticMethodOverrideMap());

		this.sourceFileToIndex = new HashMap<>();

		this.fileReferences = new FileReferences();
		this.crossReference = new TokenCrossReference();

		this.nameToTypeNoMap = new NameToTypeNoMap();
	}

	@Override
	public int addFile(String file, int[] types) {

		if (sourceFileNo >= BitDefs.MAX_SOURCE_FILE) {
			throw new IllegalStateException();
		}

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

		this.sourceFiles = ArrayAllocation.allocateArray(sourceFiles, sourceFileNo, String[]::new);

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

	String getFileName(int sourceFileNo) {

	    return sourceFiles[sourceFileNo];
	}

	int getFileNo(String fileName) {

	    final Integer fileNo = sourceFileToIndex.get(fileName);

	    return fileNo != null ? fileNo : -1;
	}

	@Override
	public void addTypeMapping(TypeName name, int typeNo) {
		nameToTypeNoMap.addMapping(name, typeNo);
	}

	@Override
	public Integer getTypeNoByTypeName(TypeName typeName) {
		return nameToTypeNoMap.getType(typeName);
	}

	@Override
    public TypeName getTypeName(int typeNo) {
        return nameToTypeNoMap.getTypeName(typeNo);
    }

    @Override
    public TypesMap<TypeName> makeTypesMap() {

	    return nameToTypeNoMap.makeTypesMap();
    }

    @Override
	public int addToken(int sourceFile, int parseTreeRef) {

		if (    sourceFiles == null
		     || sourceFile >= sourceFiles.length
	         || sourceFiles[sourceFile] == null) {

			throw new IllegalArgumentException();
		}

		return crossReference.addToken(sourceFile, parseTreeRef);
	}

	@Override
	public int getTokenForParseTreeRef(int sourceFile, int parseTreeRef) {
		return crossReference.getTokenForParseTreeRef(sourceFile, parseTreeRef);
	}

	@Override
	public int getParseTreeRefForToken(int token) {
		return crossReference.getParseTreeRefForToken(token);
	}

	@Override
	public int getVariableDeclarationTokenReferencedFrom(int fromReferenceToken) {
		return crossReference.getVariableDeclarationTokenReferencedFrom(fromReferenceToken);
	}

	@Override
	public void addTokenVariableReference(int fromToken, int toDeclarationToken) {
		crossReference.addTokenVariableReference(fromToken, toDeclarationToken);
	}

    @Override
    public int getMethodDeclarationTokenReferencedFrom(int fromReferenceToken) {
        return crossReference.getMethodDeclarationTokenReferencedFrom(fromReferenceToken);
    }

    @Override
    public String toString() {
        return "IntCompilerCodeMap [nameToTypeNoMap=" + nameToTypeNoMap + "]";
    }
}
