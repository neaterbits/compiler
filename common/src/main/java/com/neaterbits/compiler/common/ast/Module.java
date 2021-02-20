package com.neaterbits.compiler.common.ast;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ModuleSpec;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.parser.ParsedFile;

public final class Module extends BaseASTElement {

	private final ModuleSpec moduleSpec;
	private final ASTList<ParsedFile> parsedFiles;
	
	public Module(ModuleSpec moduleSpec, List<ParsedFile> parsedFiles) {
		super(null);

		Objects.requireNonNull(parsedFiles);
		
		this.moduleSpec = moduleSpec;
		this.parsedFiles = makeList(parsedFiles);
	}
	
	public Module(ModuleSpec moduleSpec, ParsedFile parsedFiles) {
		this(moduleSpec, Arrays.asList(parsedFiles));
	}

	public ModuleSpec getModuleSpec() {
		return moduleSpec;
	}

	public ASTList<ParsedFile> getParsedFiles() {
		return parsedFiles;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(parsedFiles, recurseMode, visitor);
	}
}
