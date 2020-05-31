package com.neaterbits.compiler.java.resolve;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.java.BaseCompilerTest;
import com.neaterbits.compiler.java.CompileFileCollector;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.CompiledAndMappedFiles;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFile;

public class ResolveImportedTypesTest extends BaseCompilerTest {

	final NameFileSpec refererSpec = new NameFileSpec("Referer.java");
	
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
	
	private CompiledAndResolvedFile compile(String refererSource) throws IOException {

		final CompiledAndResolvedFile referer = new CompileFileCollector()
				.add(refererSpec, refererSource)
				.add("Refered.java", referedSource)
				.compile(new TestResolvedTypes())
				.getFile(refererSpec);

		assertThat(referer).isNotNull();
		assertThat(referer.getErrors().isEmpty()).isTrue();
		
		return referer;
	}

	@Test
	public void testImportJavaLangType() throws IOException {

		final String source =
				  "package com.test;\n"
				+ "class JavaLangTestClass {\n"
				+ " private Integer integer;\n"
				+ "}\n";
		
		final TypeName integerType = new TypeName(new String [] { "java", "lang" }, null, "Integer");
		
		final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
		
		final int integerTypeNo = codeMap.addType(TypeVariant.CLASS, null, null);
		
		codeMap.addMapping(integerType, integerTypeNo);
		
		final NameFileSpec fileSpec = new NameFileSpec("JavaLangTestClass.java");
		
		final CompiledAndMappedFiles compiledAndMapped = compileAndMap(
				fileSpec,
				source,
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
	public void testImportDirectlyOtherCompiledType() throws IOException {

		final CompiledAndResolvedFile referer = compile(makeReferer("import com.test.imported.Refered;\n"));

		final ClassDataFieldMember fieldMember = getNext(referer.getASTElements(BaseASTElement.class).iterator(), ClassDataFieldMember.class);
		assertThat(fieldMember).isNotNull();
		
		assertThat(fieldMember.getInitializer(0).getNameString()).isEqualTo("refered");
		assertThat(fieldMember.getType().getTypeName())
				.isEqualTo(new TypeName(new String [] { "com", "test", "imported" }, null, "Refered"));
		
	}

	@Test
	public void testImportOnDemandOtherCompiledType() throws IOException {

		final CompiledAndResolvedFile referer = compile(makeReferer("import com.test.imported.*;\n"));

		final ClassDataFieldMember fieldMember = getNext(referer.getASTElements(BaseASTElement.class).iterator(), ClassDataFieldMember.class);
		assertThat(fieldMember).isNotNull();
		
		assertThat(fieldMember.getInitializer(0).getNameString()).isEqualTo("refered");
		assertThat(fieldMember.getType().getTypeName())
				.isEqualTo(new TypeName(new String [] { "com", "test", "imported" }, null, "Refered"));
		
	}
}
