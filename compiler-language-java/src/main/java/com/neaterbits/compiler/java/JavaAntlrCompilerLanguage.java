package com.neaterbits.compiler.java;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.parser.ASTParsedFile;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;
import com.neaterbits.compiler.resolver.ast.ASTModelImpl;
import com.neaterbits.compiler.resolver.ast.model.ObjectProgramModel;
import com.neaterbits.compiler.resolver.ast.passes.FindTypeDependenciesPass;
import com.neaterbits.compiler.resolver.passes.AddToCodeMapPass;
import com.neaterbits.compiler.resolver.passes.ReplaceResolvedTypeReferencesPass;
import com.neaterbits.compiler.resolver.passes.ResolveTypeDependenciesPass;
import com.neaterbits.compiler.util.language.CompilerLanguage;
import com.neaterbits.compiler.util.model.CompilationUnitModel;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFiles;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.compiler.util.passes.CompilerBuilderIntermediate;
import com.neaterbits.compiler.util.passes.FileParsePassInput;
import com.neaterbits.compiler.util.passes.LanguageCompiler;

public class JavaAntlrCompilerLanguage extends CompilerLanguage<CompilationUnit, ASTParsedFile> {

	@Override
	public Parser<CompilationUnit> getParser() {
		return new Java8AntlrParser(false);
	}
	
	@Override
	protected CompilerBuilderIntermediate<ASTParsedFile, FileParsePassInput<CompilationUnit>> buildCompilerParsePass() {

		return buildCompilerParsePass(

				(fileSpec, compilationUnit, errors, log) -> new ASTParsedFile(fileSpec, errors, log, compilationUnit),
				ASTParsedFile::getFileSpec);
	}

	@Override
	public LanguageCompiler<
				FileParsePassInput<CompilationUnit>,
				CompiledAndResolvedFiles
			>
	
			makeCompilerPasses(ResolvedTypes resolvedTypes) {

		final CompilationUnitModel<CompilationUnit> compilationUnitModel = new ObjectProgramModel();
		
		final ASTModelImpl typesModel = new ASTModelImpl();
		
		return buildCompilerParsePass()
		
				.addSingleToMultiPass(new FindTypeDependenciesPass())
				
				.addMultiPass(new ResolveTypeDependenciesPass<>(
							compilationUnitModel,
							JavaTypes.getBuiltinTypes(),
							resolvedTypes::lookup,
							typesModel))
				
				.addMultiPass(new ReplaceResolvedTypeReferencesPass<>(typesModel))
				.addMultiPass(new AddToCodeMapPass<>(typesModel))
				
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
