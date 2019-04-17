package com.neaterbits.compiler.java.resolve;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.java.BaseCompilerTest;
import com.neaterbits.compiler.java.CompileFileCollector;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.TypeName;
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
		
		final CompiledAndResolvedFile compiled = compile(
				"JavaLangTestClass.java",
				source,
				new TestResolvedTypes()
					.addType("java.lang.Integer"));
		
		assertThat(compiled.getErrors()).isEmpty();
		
		final ClassDataFieldMember fieldMember = getNext(compiled.getASTElements(BaseASTElement.class).iterator(), ClassDataFieldMember.class);
		assertThat(fieldMember).isNotNull();
		
		assertThat(fieldMember.getNameString()).isEqualTo("integer");
		assertThat(fieldMember.getType().getTypeName())
				.isEqualTo(new TypeName(new String [] { "java", "lang" }, null, "Integer"));

	}

	@Test
	public void testImportDirectlyOtherCompiledType() throws IOException {

		final CompiledAndResolvedFile referer = compile(makeReferer("import com.test.imported.Refered;\n"));

		final ClassDataFieldMember fieldMember = getNext(referer.getASTElements(BaseASTElement.class).iterator(), ClassDataFieldMember.class);
		assertThat(fieldMember).isNotNull();
		
		assertThat(fieldMember.getNameString()).isEqualTo("refered");
		assertThat(((NamedType)fieldMember.getType().getType()).getTypeName())
				.isEqualTo(new TypeName(new String [] { "com", "test", "imported" }, null, "Refered"));
		
	}

	@Test
	public void testImportOnDemandOtherCompiledType() throws IOException {

		final CompiledAndResolvedFile referer = compile(makeReferer("import com.test.imported.*;\n"));

		final ClassDataFieldMember fieldMember = getNext(referer.getASTElements(BaseASTElement.class).iterator(), ClassDataFieldMember.class);
		assertThat(fieldMember).isNotNull();
		
		assertThat(fieldMember.getNameString()).isEqualTo("refered");
		assertThat(((NamedType)fieldMember.getType().getType()).getTypeName())
				.isEqualTo(new TypeName(new String [] { "com", "test", "imported" }, null, "Refered"));
		
	}
}
