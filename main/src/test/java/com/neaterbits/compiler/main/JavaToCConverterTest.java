package com.neaterbits.compiler.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.neaterbits.compiler.c.emit.CCompilationUnitEmmiter;
import com.neaterbits.compiler.common.ModuleId;
import com.neaterbits.compiler.common.SourceModuleSpec;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.StructName;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterState;
import com.neaterbits.compiler.common.convert.ootofunction.OOToProceduralConverter;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.DirectoryParser;
import com.neaterbits.compiler.common.parser.FileTypeParser;
import com.neaterbits.compiler.common.parser.ParsedFile;
import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.compiler.java.emit.JavaCompilationUnitEmitter;
import com.neaterbits.compiler.java.parser.JavaParserListener;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaToCConverterTest extends BaseJavaCompilerTest {

	@Test
	public void testJavaToC() throws IOException {

		// final String fileName = "src/test/java/com/neaterbits/compiler/main/JavaToCConverterTest.java";
		//final String fileName = "../common/src/main/java/com/neaterbits/compiler/common/ModuleSpec.java";
		
		final String fileName = "src/test/java/com/neaterbits/compiler/main/IfStatementTestClass.java";

		final CompilationUnit compilationUnit = compile(fileName);
		
		final JavaCompilationUnitEmitter javaEmitter = new JavaCompilationUnitEmitter();
		
		final String emitted = emitCompilationUnit(compilationUnit, javaEmitter);
		
		System.out.println("Java code:\n" + emitted);

		final CompilationUnit cCode = convert(compilationUnit);

		final CCompilationUnitEmmiter cEmitter = new CCompilationUnitEmmiter();
		
		final String cSourceCode = emitCompilationUnit(cCode, cEmitter);
		
		System.out.println("Emitted code:\n" + cSourceCode);
	}

	@Test
	public void testConvertCompiler() throws IOException {
		
		final ModuleId common = new ModuleId("common");
		final ModuleId java = new ModuleId("java");

		final File baseDirectory = new File("..").getCanonicalFile();

		System.out.println("## baseDirectory: " + baseDirectory);

		final SourceModuleSpec commonModuleSpec = new SourceModuleSpec(
				common,
				Collections.emptyList(),
				new File(baseDirectory, "common/src/main/java"));
		
		final SourceModuleSpec javaModuleSpec = new SourceModuleSpec(
				java,
				Arrays.asList(),
				new File(baseDirectory, "java/src/main/java"));
		
		final FileTypeParser<JavaParserListener> javaParser = new FileTypeParser<>(
				new Java8AntlrParser(true),
				logger -> new JavaParserListener(logger), 
				".java");

		final DirectoryParser directoryParser = new DirectoryParser(javaParser);
		
		final List<ParsedFile> parsedFiles = directoryParser.parseDirectory(
				commonModuleSpec.getBaseDirectory(),
				new ParseLogger(System.out));
		
		assertThat(parsedFiles.size()).isGreaterThan(0);
	}

	
	private CompilationUnit convert(CompilationUnit javaCompilationUnit) {
		final OOToProceduralConverter converter = new OOToProceduralConverter();

		final OOToProceduralConverterState converterState = new OOToProceduralConverterState() {
			@Override
			public FunctionName methodToFunctionName(Namespace namespace, MethodName methodName) {
				return new FunctionName(Strings.join(namespace.getParts(), '_') + '_' + methodName.getName());
			}

			@Override
			public StructName classToStructName(Namespace namespace, ClassName className) {
				return new StructName(Strings.join(namespace.getParts(), '_') + '_' + className.getName());
			}
		};
		
		final CompilationUnit cCode = converter.convertCompilationUnit(javaCompilationUnit, converterState);
		
		return cCode;
	}
}
