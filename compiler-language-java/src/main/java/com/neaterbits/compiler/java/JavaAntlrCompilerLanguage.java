package com.neaterbits.compiler.java;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.parser.ASTParsedFile;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;
import com.neaterbits.compiler.resolver.ast.ASTModelImpl;
import com.neaterbits.compiler.resolver.ast.passes.FindTypeDependenciesPass;
import com.neaterbits.compiler.resolver.passes.AddTypesAndMembersToCodeMapPass;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.resolver.passes.ReplaceResolvedTypeReferencesPass;
import com.neaterbits.compiler.resolver.passes.ResolveTypeDependenciesPass;
import com.neaterbits.compiler.resolver.passes.namereferenceresolve.NameReferenceResolvePass;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;
import com.neaterbits.compiler.util.model.CompilationUnitModel;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.compiler.util.passes.CompilerBuilderIntermediate;
import com.neaterbits.compiler.util.passes.FileParsePassInput;
import com.neaterbits.compiler.util.passes.LanguageCompiler;

public class JavaAntlrCompilerLanguage extends CompilerLanguage<CompilationUnit, ASTParsedFile, CodeMapCompiledAndMappedFiles<CompilationUnit>> {

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
				CodeMapCompiledAndMappedFiles<CompilationUnit>
			>
	
			makeCompilerPasses(ResolvedTypes resolvedTypes, CompilerCodeMap codeMap) {

		final JavaProgramModel compilationUnitModel = new JavaProgramModel();
		
		final CompilationUnitModel<CompilationUnit> model = compilationUnitModel;
		
		final ASTModelImpl typesModel = new ASTModelImpl();
		
		return buildCompilerParsePass()
		
				.addSingleToMultiPass(new FindTypeDependenciesPass())
				
				.addMultiPass(new ResolveTypeDependenciesPass<>(
							compilationUnitModel,
							JavaTypes.getBuiltinTypes(),
							resolvedTypes::lookup,
							typesModel))
				
				.addMultiPass(new ReplaceResolvedTypeReferencesPass<>(resolvedTypes::lookup, typesModel))
				.addMultiPass(new AddTypesAndMembersToCodeMapPass<>(codeMap, typesModel))
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
