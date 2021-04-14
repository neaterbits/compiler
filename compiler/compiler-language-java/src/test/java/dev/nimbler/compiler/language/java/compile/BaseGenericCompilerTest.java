package dev.nimbler.compiler.language.java.compile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.language.java.TestFile;
import dev.nimbler.compiler.language.java.compile.CompileFileCollector.CompileFile;
import dev.nimbler.compiler.language.java.compile.sourcebuilder.SourceTestFilesBuilder;
import dev.nimbler.compiler.language.java.resolve.TestResolvedTypes;
import dev.nimbler.compiler.resolver.build.ModulesBuilder;
import dev.nimbler.compiler.resolver.util.CompilerLanguage;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;

public abstract class BaseGenericCompilerTest<
            COMPILATION_UNIT,
            PARSED_FILE extends ParsedFile> {
    
    protected abstract CompilerLanguage<COMPILATION_UNIT, PARSED_FILE> 
        createCompilerLanguage(GetBuiltinTypeNo getBuiltinTypeNo);

	protected final CompiledAndResolvedFile compile(String file, String text, TestResolvedTypes resolvedTypes) throws IOException, ParserException {
		
		Objects.requireNonNull(file);

		final TestFile testFile = new TestFile(file, text);

		return compile(testFile, resolvedTypes);
	}

	protected final CompiledAndResolvedFile compile(TestFile testFile, TestResolvedTypes resolvedTypes)
	        throws IOException, ParserException {
	    
		Objects.requireNonNull(testFile);
		
		return new CompileFileCollector<>(this::compileFiles)
				.add(testFile)
				.compile(resolvedTypes)
				.getFile(testFile.getName());
		
	}

	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileAndMap(
	        TestFile testFile,
	        TestResolvedTypes resolvedTypes,
            IntCompilerCodeMap codeMap) throws IOException, ParserException {
	    
	    return compileAndMap(testFile.getName(), testFile.getText(), resolvedTypes, codeMap);
	}

	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileAndMap(
			String name,
			String text,
			TestResolvedTypes resolvedTypes,
			IntCompilerCodeMap codeMap) throws IOException, ParserException {
		
		Objects.requireNonNull(name);
		Objects.requireNonNull(text);
		
		return new CompileFileCollector<>(this::compileFiles)
				.add(name, text)
				.compile(resolvedTypes, codeMap);
	}
	
	protected final CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileFiles(
			List<CompileFile> files,
			IntCompilerCodeMap codeMap) throws IOException, ParserException {
	    
        final CompilerLanguage<COMPILATION_UNIT, PARSED_FILE> compilerLanguage
            = createCompilerLanguage(codeMap::getTypeNoByTypeName);

        ModulesBuilder.addBuiltinTypesToCodeMap(
                compilerLanguage.getLanguageSpec(),
                codeMap);

	    final TestFilesBuilder<COMPILATION_UNIT, PARSED_FILE> builder
	        = new SourceTestFilesBuilder<>();
	    
	    return builder.compileFiles(files, codeMap, compilerLanguage);
	}

	@SuppressWarnings("unchecked")
	protected static <T extends BaseASTElement> T get(List<BaseASTElement> elements, int index) {
		return (T)elements.get(index);
	}

	@SuppressWarnings("unchecked")
	protected static <T extends BaseASTElement> T get(Iterator<BaseASTElement> iterator) {
		return (T)iterator.next();
	}

	@SuppressWarnings("unchecked")
	protected static <T extends BaseASTElement> T getNext(Iterator<BaseASTElement> iterator, Class<T> elementType) {

		while (iterator.hasNext()) {
			
			final BaseASTElement next = iterator.next();
			
			if (next.getClass().equals(elementType)) {
				return (T)next;
			}
		}
		
		return null;
	}
}
