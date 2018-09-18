package com.neaterbits.compiler.common.ast;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.parser.ParsedFile;

public final class Module extends BaseASTElement {

	private final ASTList<ParsedFile> parsedFiles;
	
	public Module(List<ParsedFile> parsedFiles) {
		super(null);

		this.parsedFiles = makeList(parsedFiles);
	}
	
	public Module(ParsedFile parsedFiles) {
		this(Arrays.asList(parsedFiles));
	}

	public ASTList<ParsedFile> getParsedFiles() {
		return parsedFiles;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(parsedFiles, recurseMode, visitor);
	}
}
