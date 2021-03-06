package dev.nimbler.compiler.language.java.resolve;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.jutils.parse.ParserException;

import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.language.java.JavaProgramModel;
import dev.nimbler.compiler.language.java.TestFile;
import dev.nimbler.compiler.language.java.compile.BaseCompilerTest;
import dev.nimbler.compiler.language.java.compile.CodeMapCompiledAndMappedFiles;
import dev.nimbler.compiler.language.java.compile.CompileFileCollector;
import dev.nimbler.compiler.model.common.ISourceToken;
import dev.nimbler.compiler.model.common.SourceTokenType;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;

public class CrossReferenceTest extends BaseCompilerTest {

	@Test
	public void testLocalVariableNameReference() throws IOException, ParserException {

		final String source =
				
				"package com.test;\n"
			+   "class LocalVarNameTest {\n"
			+   " static void testMethod() {\n"
			+   " 	int integerVariable = 1;\n"
			+   "   int xyz = integerVariable;\n"
			// +   "   integerVariable = 12345;\n"
			+   "  }\n"
			+   "}\n";

		final TestFile spec = new TestFile("FieldsTest.java", source);
		
		final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
		
		final TestResolvedTypes resolvedTypes = new TestResolvedTypes();
		
		final CodeMapCompiledAndMappedFiles<CompilationUnit> compiledAndMapped
				= compileAndMap(spec, resolvedTypes, codeMap);
		
		// final TypeName type = JavaUtil.parseToTypeName("com.test.FieldsTest");
		
		final int variableDeclarationOffset = source.indexOf("integerVariable");

		final JavaProgramModel programModel = new JavaProgramModel();
		
		final CompilationUnit compilationUnit = compiledAndMapped.getCompilationUnit(spec);
		
		assertThat(compilationUnit).isNotNull();

		final ISourceToken varDeclarationSourceToken = programModel.getTokenAtOffset(compilationUnit, variableDeclarationOffset, resolvedTypes);
		assertThat(varDeclarationSourceToken).isNotNull();

		assertThat(varDeclarationSourceToken.getTokenType()).isEqualTo(SourceTokenType.LOCAL_VARIABLE_DECLARATION_NAME);

		final int variableReferenceOffset = source.indexOf("integerVariable", variableDeclarationOffset + 1);
		
		final ISourceToken varReferenceSourceToken = programModel.getTokenAtOffset(compilationUnit, variableReferenceOffset, resolvedTypes);
		assertThat(varReferenceSourceToken).isNotNull();
		assertThat(varReferenceSourceToken.getTokenType()).isEqualTo(SourceTokenType.VARIABLE_REFERENCE);
		
		// Cross reference tests
		final int sourceFile = compiledAndMapped.getSourceFileNo(spec);
		assertThat(sourceFile).isGreaterThanOrEqualTo(0);
		
		final int varDeclarationToken = codeMap.getTokenForParseTreeRef(sourceFile, varDeclarationSourceToken.getParseTreeReference());
		assertThat(varDeclarationToken).isGreaterThanOrEqualTo(0);
		
		final int varReferenceToken = codeMap.getTokenForParseTreeRef(sourceFile, varReferenceSourceToken.getParseTreeReference());
		
		assertThat(codeMap.getVariableDeclarationTokenReferencedFrom(varReferenceToken)).isEqualTo(varDeclarationToken);
	}

	@Test
	public void testLocalVariableNameAssignmentReference() throws IOException, ParserException {

		final String source =
				
				"package com.test;\n"
			+   "class LocalVarNameTest {\n"
			+   " static void testMethod() {\n"
			+   " 	int integerVariable = 1;\n"
			+   "   integerVariable = 12345;\n"
			+   "  }\n"
			+   "}\n";

		
		final TestFile spec = new TestFile("FieldsTest.java", source);
		
		final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
		
		final TestResolvedTypes resolvedTypes = new TestResolvedTypes();
		
		final CodeMapCompiledAndMappedFiles<CompilationUnit> compiledAndMapped
				= compileAndMap(spec, resolvedTypes, codeMap);
		
		// final TypeName type = JavaUtil.parseToTypeName("com.test.FieldsTest");
		
		final int variableDeclarationOffset = source.indexOf("integerVariable");

		final JavaProgramModel programModel = new JavaProgramModel();
		
		final CompilationUnit compilationUnit = compiledAndMapped.getCompilationUnit(spec);

		programModel.print(compilationUnit, System.out);
		
		assertThat(compilationUnit).isNotNull();

		final ISourceToken varDeclarationSourceToken = programModel.getTokenAtOffset(compilationUnit, variableDeclarationOffset, resolvedTypes);
		assertThat(varDeclarationSourceToken).isNotNull();

		assertThat(varDeclarationSourceToken.getTokenType()).isEqualTo(SourceTokenType.LOCAL_VARIABLE_DECLARATION_NAME);

		final int variableReferenceOffset = source.indexOf("integerVariable", variableDeclarationOffset + 1);
		
		final ISourceToken varReferenceSourceToken = programModel.getTokenAtOffset(
		                                                                compilationUnit,
		                                                                variableReferenceOffset,
		                                                                resolvedTypes);

		assertThat(varReferenceSourceToken).isNotNull();
		/*
		assertThat(varReferenceSourceToken.getTokenType()).isEqualTo(SourceTokenType.VARIABLE_REFERENCE);
		
		// Cross reference tests
		final int sourceFile = compiledAndMapped.getSourceFileNo(spec);
		assertThat(sourceFile).isGreaterThan(0);
		
		final int varDeclarationToken = codeMap.getTokenForParseTreeRef(sourceFile, varDeclarationSourceToken.getParseTreeReference());
		assertThat(varDeclarationToken).isGreaterThan(0);
		
		final int varReferenceToken = codeMap.getTokenForParseTreeRef(sourceFile, varReferenceSourceToken.getParseTreeReference());
		
		assertThat(codeMap.getVariableDeclarationTokenReferencedFrom(varReferenceToken)).isEqualTo(varDeclarationToken);
		*/
	}

	@Test
	public void testLocalVariablePrimaryListNameReference() throws IOException, ParserException {

		final String mainClassSource =
				"package com.test;\n"
			+   "class LocalVarNameTest {\n"

			+   " static void testMethod() {\n"
			+   " 	TestField fieldTest = new TestField();\n"
			+   "   int variable = fieldTest.xyz;\n"
			// +   "   integerVariable = 12345;\n"
			+   "  }\n"
			+   "}\n";


		final String fieldClassSource =
				"package com.test;\n"
			+   "class TestField {\n"
			+   " int xyz;"
			+   "}\n";

		
		final TestFile mainClass = new TestFile("LocalVarNameTest.java", mainClassSource);
		final TestFile fieldClass = new TestFile("TestField.java", fieldClassSource);

		final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
		
		final TestResolvedTypes resolvedTypes = new TestResolvedTypes();
		
		final CodeMapCompiledAndMappedFiles<CompilationUnit> compiledAndMapped
				= new CompileFileCollector<>(this::compileFiles)
				.add(mainClass)
				.add(fieldClass)
				.compile(resolvedTypes, codeMap);
		
		// final TypeName type = JavaUtil.parseToTypeName("com.test.FieldsTest");
		
		final int variableDeclarationOffset = mainClassSource.indexOf("fieldTest");

		final JavaProgramModel programModel = new JavaProgramModel();
		
		final CompilationUnit compilationUnit = compiledAndMapped.getCompilationUnit(mainClass);

		programModel.print(compilationUnit, System.out);
		
		assertThat(compilationUnit).isNotNull();

		final ISourceToken varDeclarationSourceToken = programModel.getTokenAtOffset(compilationUnit, variableDeclarationOffset, resolvedTypes);
		assertThat(varDeclarationSourceToken).isNotNull();

		assertThat(varDeclarationSourceToken.getTokenType())
		    .isEqualTo(SourceTokenType.LOCAL_VARIABLE_DECLARATION_NAME);

		final int variableReferenceOffset = mainClassSource.indexOf("fieldTest", variableDeclarationOffset + 1);
		
		final ISourceToken varReferenceSourceToken = programModel.getTokenAtOffset(compilationUnit, variableReferenceOffset, resolvedTypes);
		assertThat(varReferenceSourceToken).isNotNull();
		assertThat(varReferenceSourceToken.getTokenType()).isEqualTo(SourceTokenType.VARIABLE_REFERENCE);
		
		// Cross reference tests
		final int sourceFile = compiledAndMapped.getSourceFileNo(mainClass);
		assertThat(sourceFile).isGreaterThanOrEqualTo(0);
		
		final int varDeclarationToken = codeMap.getTokenForParseTreeRef(sourceFile, varDeclarationSourceToken.getParseTreeReference());
		assertThat(varDeclarationToken).isGreaterThanOrEqualTo(0);
		
		final int varReferenceToken = codeMap.getTokenForParseTreeRef(sourceFile, varReferenceSourceToken.getParseTreeReference());
		
		assertThat(codeMap.getVariableDeclarationTokenReferencedFrom(varReferenceToken)).isEqualTo(varDeclarationToken);
	}
}
