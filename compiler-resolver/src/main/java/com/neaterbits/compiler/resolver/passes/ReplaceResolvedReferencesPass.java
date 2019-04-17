package com.neaterbits.compiler.resolver.passes;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.compiler.resolver.ASTTypesModel;
import com.neaterbits.compiler.resolver.ResolvedReferenceReplacer;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFiles;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.passes.MultiPass;

public class ReplaceResolvedReferencesPass<PARSED_FILE extends ParsedFile, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
	extends MultiPass<
		ResolvedTypeDependencies<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>,
//		ResolvedTypeDependencies<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		CompiledAndResolvedFiles
> {

	private final ASTTypesModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typesModel;
	
	public ReplaceResolvedReferencesPass(ASTTypesModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> typesModel) {

		Objects.requireNonNull(typesModel);
		
		this.typesModel = typesModel;
	}

	@Override
	public ResolvedTypeDependencies<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> execute(
			ResolvedTypeDependencies<PARSED_FILE, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> input) throws IOException {

		ResolvedReferenceReplacer.replaceResolvedTypeReferences(input.getResolveFilesResult(), typesModel);
		
		return input;
	}
}
