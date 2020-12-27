package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.compiler.codemap.compiler.CrossReferenceUpdater;
import com.neaterbits.compiler.model.common.ParseTreeModel;
import com.neaterbits.compiler.model.common.passes.MultiPass;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.passes.ParsedModuleAndCodeMap;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.parse.ScopesListener;

public final class NameReferenceResolvePass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
		extends MultiPass<
			ParsedModuleAndCodeMap<PARSED_FILE>,
			ParsedModuleAndCodeMap<PARSED_FILE>> {
	
	private final ParseTreeModel<COMPILATION_UNIT> parseTreeModel;
	private final boolean addTokenRefs;
	
	public NameReferenceResolvePass(
	        ParseTreeModel<COMPILATION_UNIT> parseTreeModel,
	        boolean addTokenRefs) {

		Objects.requireNonNull(parseTreeModel);
		
		this.parseTreeModel = parseTreeModel;
		this.addTokenRefs = addTokenRefs;
	}

	@Override
	public ParsedModuleAndCodeMap<PARSED_FILE> execute(ParsedModuleAndCodeMap<PARSED_FILE> input) {

		final List<ResolveError> errors = new ArrayList<>();
		
		for (ParsedWithCachedRefs<PARSED_FILE, ResolveError> file : input.getParsed()) {
			
			final int sourceFileNo = file.getCodeMapFileNo();
			
			resolveFile(file.getParsedFile(), input.getCodeMap(), sourceFileNo, errors);
		}
		
		return input;
	}

	private void resolveFile(ParsedFile parsedFile, CrossReferenceUpdater crossReference, int sourceFile, List<ResolveError> errors) {
		
		final COMPILATION_UNIT compilationUnit = parsedFile.getCompilationUnit();
		
		final ScopesListener scopesListener = new NameReferenceResolveVisitor(
		                                                        crossReference,
		                                                        sourceFile,
		                                                        addTokenRefs,
		                                                        errors);   
		        
		parseTreeModel.iterateScopesAndVariables(compilationUnit, scopesListener);
	}
}
