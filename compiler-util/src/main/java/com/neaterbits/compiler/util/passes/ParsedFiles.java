package com.neaterbits.compiler.util.passes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ParsedFiles<PARSED_FILE> extends MultiFileInputOutput<PARSED_FILE> {

	private final List<PARSED_FILE> parsedFiles;
	
	public ParsedFiles(List<PARSED_FILE> parsedFiles) {
		this.parsedFiles = Collections.unmodifiableList(new ArrayList<>(parsedFiles));
	}

	public List<PARSED_FILE> getParsedFiles() {
		return parsedFiles;
	}
}
