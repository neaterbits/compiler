package com.neaterbits.compiler.java;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaTypes;
import com.neaterbits.compiler.resolver.ast.objects.passes.FindTypeDependenciesPass;
import com.neaterbits.compiler.resolver.passes.AddTypesAndMembersToCodeMapPass;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.resolver.passes.LibraryTypes;
import com.neaterbits.compiler.resolver.passes.ReplaceResolvedTypeReferencesPass;
import com.neaterbits.compiler.resolver.passes.ResolveTypeDependenciesPass;
import com.neaterbits.compiler.resolver.passes.namereferenceresolve.NameReferenceResolvePass;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;
import com.neaterbits.compiler.util.CastFullContextProvider;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.CompilationUnitModel;
import com.neaterbits.compiler.util.model.LibraryTypeRef;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.model.TypeSources;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.compiler.util.passes.CompilerBuilderIntermediate;
import com.neaterbits.compiler.util.passes.FileParsePassInput;
import com.neaterbits.compiler.util.passes.LanguageCompiler;

public class JavaAntlrCompilerLanguage extends CompilerLanguage<CompilationUnit, ASTParsedFile, CodeMapCompiledAndMappedFiles<CompilationUnit>> {

    private static final FullContextProvider FULL_CONTEXT_PROVIDER = CastFullContextProvider.INSTANCE;
	@Override
	public Parser<CompilationUnit> getParser() {
		return new Java8AntlrParser(false);
	}
	
	@Override
	protected CompilerBuilderIntermediate<ASTParsedFile, FileParsePassInput<CompilationUnit>> buildCompilerParsePass() {

		return buildCompilerParsePass(

				(fileSpec, compilationUnit, errors, log) -> new ASTParsedFile(fileSpec, errors, log, compilationUnit),
				ASTParsedFile::getFileSpec,
				FULL_CONTEXT_PROVIDER);
	}

	@Override
	public LanguageCompiler<
				FileParsePassInput<CompilationUnit>,
				CodeMapCompiledAndMappedFiles<CompilationUnit>
			>
	
			makeCompilerPasses(ResolvedTypes resolvedTypes, CompilerCodeMap codeMap) {

		final JavaProgramModel compilationUnitModel = new JavaProgramModel(FULL_CONTEXT_PROVIDER);
		
		final CompilationUnitModel<CompilationUnit> model = compilationUnitModel;
		
		final LibraryTypes libraryTypes = scopedName -> {
			final TypeName typeName = resolvedTypes.lookup(scopedName, TypeSources.LIBRARY);
			
			return typeName != null ? new LibraryTypeRef(typeName) : null;
		};
		
		return buildCompilerParsePass()
		
				.addSingleToMultiPass(new FindTypeDependenciesPass())
				
				.addMultiPass(new ResolveTypeDependenciesPass<>(
							compilationUnitModel,
							JavaTypes.getBuiltinTypeRefs(),
							libraryTypes,
							compilationUnitModel))
				
				.addMultiPass(new ReplaceResolvedTypeReferencesPass<>(libraryTypes, compilationUnitModel))
				.addMultiPass(new AddTypesAndMembersToCodeMapPass<>(codeMap, compilationUnitModel))
				.addMultiPass(new NameReferenceResolvePass<>(model))
				
				.build();

		
	}

	/*
	@Override
	public LanguageCompiler<
			FileParsePassInput<CompilationUnit>,
			CompiledFile<ComplexType<?, ?, ?>, CompilationUnit>>
	
		makeCompiler() {
		
	}
	*/
}
