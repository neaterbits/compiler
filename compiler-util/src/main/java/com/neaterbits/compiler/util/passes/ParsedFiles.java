package com.neaterbits.compiler.util.passes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.ParsedFile;

public class ParsedFiles<PARSED_FILE extends ParsedFile> extends MultiFileInputOutput<PARSED_FILE> {

	private final List<PARSED_FILE> parsedFiles;
	
	public ParsedFiles(List<PARSED_FILE> parsedFiles) {
		this.parsedFiles = Collections.unmodifiableList(new ArrayList<>(parsedFiles));
	}
	
	protected ParsedFiles(ParsedFiles<PARSED_FILE> other) {
		this(other.parsedFiles);
	}

	public final List<PARSED_FILE> getParsedFiles() {
		return parsedFiles;
	}
	
	public final PARSED_FILE getParsedFile(FileSpec fileSpec) {
		
		Objects.requireNonNull(fileSpec);
		
		final PARSED_FILE parsedFile = parsedFiles.stream()
				.filter(file -> file.getFileSpec().equals(fileSpec))
				.findFirst()
				.orElse(null);

		return parsedFile;
	}
}
