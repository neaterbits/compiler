package com.neaterbits.compiler.java.resolve;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.java.BaseCompilerTest;
import com.neaterbits.compiler.java.CompileFileCollector;
import com.neaterbits.compiler.java.JavaProgramModel;
import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.model.common.SourceTokenType;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.util.parse.ParserException;

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

		
		final NameFileSpec spec = new NameFileSpec("FieldsTest.java");
		
		final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
		
		final ResolvedTypes resolvedTypes = new TestResolvedTypes();
		
		final CodeMapCompiledAndMappedFiles<CompilationUnit> compiledAndMapped
				= compileAndMap(spec, source, resolvedTypes, codeMap);
		
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
		assertThat(sourceFile).isGreaterThan(0);
		
		final int varDeclarationToken = codeMap.getTokenForParseTreeRef(sourceFile, varDeclarationSourceToken.getParseTreeReference());
		assertThat(varDeclarationToken).isGreaterThan(0);
		
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

		
		final NameFileSpec spec = new NameFileSpec("FieldsTest.java");
		
		final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
		
		final ResolvedTypes resolvedTypes = new TestResolvedTypes();
		
		final CodeMapCompiledAndMappedFiles<CompilationUnit> compiledAndMapped
				= compileAndMap(spec, source, resolvedTypes, codeMap);
		
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
		assertThat(sourceFile).isGreaterThan(0);
		
		final int varDeclarationToken = codeMap.getTokenForParseTreeRef(sourceFile, varDeclarationSourceToken.getParseTreeReference());
		assertThat(varDeclarationToken).isGreaterThan(0);
		
		final int varReferenceToken = codeMap.getTokenForParseTreeRef(sourceFile, varReferenceSourceToken.getParseTreeReference());
		
		assertThat(codeMap.getVariableDeclarationTokenReferencedFrom(varReferenceToken)).isEqualTo(varDeclarationToken);
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

		
		final NameFileSpec mainClass = new NameFileSpec("LocalVarNameTest.java");
		final NameFileSpec fieldClass = new NameFileSpec("TestField.java");

		final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
		
		final ResolvedTypes resolvedTypes = new TestResolvedTypes();
		
		final CodeMapCompiledAndMappedFiles<CompilationUnit> compiledAndMapped
				= new CompileFileCollector<>(this::compileFiles)
				.add(mainClass, mainClassSource)
				.add(fieldClass, fieldClassSource)
				.compile(resolvedTypes, codeMap);
		
		// final TypeName type = JavaUtil.parseToTypeName("com.test.FieldsTest");
		
		final int variableDeclarationOffset = mainClassSource.indexOf("fieldTest");

		final JavaProgramModel programModel = new JavaProgramModel();
		
		final CompilationUnit compilationUnit = compiledAndMapped.getCompilationUnit(mainClass);

		programModel.print(compilationUnit, System.out);
		
		assertThat(compilationUnit).isNotNull();

		final ISourceToken varDeclarationSourceToken = programModel.getTokenAtOffset(compilationUnit, variableDeclarationOffset, resolvedTypes);
		assertThat(varDeclarationSourceToken).isNotNull();

		assertThat(varDeclarationSourceToken.getTokenType()).isEqualTo(SourceTokenType.LOCAL_VARIABLE_DECLARATION_NAME);

		final int variableReferenceOffset = mainClassSource.indexOf("fieldTest", variableDeclarationOffset + 1);
		
		final ISourceToken varReferenceSourceToken = programModel.getTokenAtOffset(compilationUnit, variableReferenceOffset, resolvedTypes);
		assertThat(varReferenceSourceToken).isNotNull();
		assertThat(varReferenceSourceToken.getTokenType()).isEqualTo(SourceTokenType.VARIABLE_REFERENCE);

		
		// Cross reference tests
		final int sourceFile = compiledAndMapped.getSourceFileNo(mainClass);
		assertThat(sourceFile).isGreaterThan(0);
		
		final int varDeclarationToken = codeMap.getTokenForParseTreeRef(sourceFile, varDeclarationSourceToken.getParseTreeReference());
		assertThat(varDeclarationToken).isGreaterThan(0);
		
		final int varReferenceToken = codeMap.getTokenForParseTreeRef(sourceFile, varReferenceSourceToken.getParseTreeReference());
		
		assertThat(codeMap.getVariableDeclarationTokenReferencedFrom(varReferenceToken)).isEqualTo(varDeclarationToken);
	}
}
