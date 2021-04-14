package com.neaterbits.compiler.java.resolve;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.java.TestFile;
import com.neaterbits.compiler.java.compile.BaseCompilerTest;
import com.neaterbits.compiler.java.compile.CompileFileCollector;
import com.neaterbits.compiler.java.compile.CompiledAndMappedFiles;
import com.neaterbits.compiler.java.compile.CompiledAndResolvedFile;
import com.neaterbits.language.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.language.common.types.TypeVariant;
import com.neaterbits.util.parse.ParserException;

public class ResolveImportedTypesTest extends BaseCompilerTest {

	final String refererFileName = "Referer.java";
	
	final String referedSource =
			
		 "package com.test.imported;\n"
		+ "class Refered {\n"
		+ "}\n";

	private static String makeReferer(String imports) {
		final String refererSource =
				
			  "package com.test;\n"
			+  imports
			+ "class Referer {\n"
			+ " private Refered refered;\n"
			+ "}\n";
		
		return refererSource;
	}
	
	private CompiledAndResolvedFile compile(String refererSource) throws IOException, ParserException {

		final CompiledAndResolvedFile referer = new CompileFileCollector<>(this::compileFiles)
				.add(refererFileName, refererSource)
				.add("Refered.java", referedSource)
				.compile(new TestResolvedTypes())
				.getFile(refererFileName);

		assertThat(referer).isNotNull();
		assertThat(referer.getErrors().isEmpty()).isTrue();
		
		return referer;
	}

	@Test
	public void testImportJavaLangType() throws IOException, ParserException {

		final String source =
				  "package com.test;\n"
				+ "class JavaLangTestClass {\n"
				+ " private Integer integer;\n"
				+ "}\n";
		
		final TypeName integerType = new TypeName(new String [] { "java", "lang" }, null, "Integer");
		
		final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
		
		final int integerTypeNo = codeMap.addType(TypeVariant.CLASS, null, null);
		
		codeMap.addTypeMapping(integerType, integerTypeNo);
		
		final TestFile fileSpec = new TestFile("JavaLangTestClass.java", source);
		
		final CompiledAndMappedFiles compiledAndMapped = compileAndMap(
				fileSpec,
				new TestResolvedTypes()
					.addType("java.lang.Integer"),
				codeMap);
		
		final CompiledAndResolvedFile compiled = compiledAndMapped.getFile(fileSpec);
		
		assertThat(compiled.getErrors()).isEmpty();
		
		final ClassDataFieldMember fieldMember = getNext(compiled.getASTElements(BaseASTElement.class).iterator(), ClassDataFieldMember.class);
		assertThat(fieldMember).isNotNull();
		
		assertThat(fieldMember.getInitializer(0).getNameString()).isEqualTo("integer");
		assertThat(fieldMember.getType().getTypeName())
				.isEqualTo(new TypeName(new String [] { "java", "lang" }, null, "Integer"));

	}

	@Test
	public void testImportDirectlyOtherCompiledType() throws IOException, ParserException {

		final CompiledAndResolvedFile referer = compile(makeReferer("import com.test.imported.Refered;\n"));

		final ClassDataFieldMember fieldMember = getNext(referer.getASTElements(BaseASTElement.class).iterator(), ClassDataFieldMember.class);
		assertThat(fieldMember).isNotNull();
		
		assertThat(fieldMember.getInitializer(0).getNameString()).isEqualTo("refered");
		assertThat(fieldMember.getType().getTypeName())
				.isEqualTo(new TypeName(new String [] { "com", "test", "imported" }, null, "Refered"));
		
	}

	@Test
	public void testImportOnDemandOtherCompiledType() throws IOException, ParserException {

		final CompiledAndResolvedFile referer = compile(makeReferer("import com.test.imported.*;\n"));

		final ClassDataFieldMember fieldMember = getNext(referer.getASTElements(BaseASTElement.class).iterator(), ClassDataFieldMember.class);
		assertThat(fieldMember).isNotNull();
		
		assertThat(fieldMember.getInitializer(0).getNameString()).isEqualTo("refered");
		assertThat(fieldMember.getType().getTypeName())
				.isEqualTo(new TypeName(new String [] { "com", "test", "imported" }, null, "Refered"));
		
	}
}
