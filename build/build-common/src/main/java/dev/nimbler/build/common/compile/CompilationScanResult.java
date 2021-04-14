package dev.nimbler.build.common.compile;

import java.util.Objects;

import dev.nimbler.build.types.compile.CompileList;
import dev.nimbler.build.types.compile.FileDependencyMap;

public final class CompilationScanResult {

	private final CompileList compileList;
	private final FileDependencyMap fileDependencyMap;
	
	public CompilationScanResult(CompileList compileList, FileDependencyMap fileDependencyMap) {

		Objects.requireNonNull(compileList);
		
		this.compileList = compileList;
		this.fileDependencyMap = fileDependencyMap;
	}

	public CompileList getCompileList() {
		return compileList;
	}

	public FileDependencyMap getFileDependencyMap() {
		return fileDependencyMap;
	}
}
