package dev.nimbler.compiler.model.common.passes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.parse.ParsedFile;

public class ParsedFiles<PARSED_FILE extends ParsedFile> extends MultiFileInputOutput<PARSED_FILE> {

	private final List<PARSED_FILE> parsedFiles;

	public ParsedFiles(Collection<PARSED_FILE> parsedFiles) {
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

    @Override
    public String toString() {
        return "ParsedFiles [parsedFiles=" + parsedFiles + "]";
    }
}
