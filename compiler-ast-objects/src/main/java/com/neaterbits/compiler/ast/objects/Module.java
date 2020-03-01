package com.neaterbits.compiler.ast.objects;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.modules.ModuleSpec;

public final class Module extends BaseASTElement {

	private final ModuleSpec moduleSpec;
	private final ASTList<ASTParsedFile> parsedFiles;
	
	public Module(ModuleSpec moduleSpec, Collection<ASTParsedFile> parsedFiles) {
		super(null);

		Objects.requireNonNull(parsedFiles);
		
		this.moduleSpec = moduleSpec;
		this.parsedFiles = makeList(parsedFiles);
	}
	
	public Module(ModuleSpec moduleSpec, ASTParsedFile parsedFiles) {
		this(moduleSpec, Arrays.asList(parsedFiles));
	}

	public ModuleSpec getModuleSpec() {
		return moduleSpec;
	}

	public ASTList<ASTParsedFile> getParsedFiles() {
		return parsedFiles;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.MODULE;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(parsedFiles, recurseMode, iterator);
	}
}
