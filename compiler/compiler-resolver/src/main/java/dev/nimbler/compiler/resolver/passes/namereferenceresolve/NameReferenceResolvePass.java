package dev.nimbler.compiler.resolver.passes.namereferenceresolve;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.build.strategies.compilemodules.ParsedWithCachedRefs;
import dev.nimbler.compiler.model.common.CompilationUnitModel;
import dev.nimbler.compiler.model.common.UnresolvedScopesListener;
import dev.nimbler.compiler.model.common.passes.MultiPass;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.resolver.passes.ParsedModuleAndCodeMap;
import dev.nimbler.compiler.util.parse.ParsedFile;

public final class NameReferenceResolvePass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
		extends MultiPass<
			ParsedModuleAndCodeMap<PARSED_FILE>,
			ParsedModuleAndCodeMap<PARSED_FILE>> {
	
	private final CompilationUnitModel<COMPILATION_UNIT> parseTreeModel;

	public NameReferenceResolvePass(CompilationUnitModel<COMPILATION_UNIT> parseTreeModel) {

		Objects.requireNonNull(parseTreeModel);
		
		this.parseTreeModel = parseTreeModel;
	}

	@Override
	public ParsedModuleAndCodeMap<PARSED_FILE> execute(ParsedModuleAndCodeMap<PARSED_FILE> input) {

		final List<ResolveError> errors = new ArrayList<>();
		
		for (ParsedWithCachedRefs<PARSED_FILE, ResolveError> file : input.getParsed()) {
		    
		    final COMPILATION_UNIT compilationUnit = file.getParsedFile().getCompilationUnit();
	        
		    final UnresolvedScopesListener scopesListener = new NameReferenceResolveVisitor<>(
                    parseTreeModel,
                    input.getCodeMap(),
                    input.getTypesMap(),
                    compilationUnit,
                    errors);

	        parseTreeModel.iterateUnresolvedScopesAndVariables(compilationUnit, scopesListener);
		}
		
		return input;
	}
}
