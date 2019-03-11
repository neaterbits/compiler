package com.neaterbits.compiler.ast;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.parser.ParsedFile;
import com.neaterbits.compiler.util.modules.ModuleSpec;

public final class Module extends BaseASTElement {

	private final ModuleSpec moduleSpec;
	private final ASTList<ParsedFile> parsedFiles;
	
	public Module(ModuleSpec moduleSpec, Collection<ParsedFile> parsedFiles) {
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
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(parsedFiles, recurseMode, iterator);
	}
}
