package com.neaterbits.compiler.java.resolve;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.CompilationCodeLines;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.FieldNameDeclaration;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.Namespace;
import com.neaterbits.compiler.ast.NamespaceDeclaration;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.ast.typedefinition.FieldModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.FieldModifiers;
import com.neaterbits.compiler.ast.typedefinition.FieldVisibility;
import com.neaterbits.compiler.ast.typereference.ComplexTypeReference;
import com.neaterbits.compiler.java.BaseCompilerTest;
import com.neaterbits.compiler.java.CompileFileCollector;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFile;

public class ResolveSamePackageTest extends BaseCompilerTest {

	final String refererSource =
			
			"package com.test;\n"
		+   "class Referer {\n"
		+   " private Refered refered;\n"
		+   "}\n";

	final NameFileSpec refererSpec = new NameFileSpec("Referer.java");
	
	final String referedSource =
			
			"package com.test;\n"
		+   "class Refered {\n"
		+   "}\n";

	@Test
	public void testResolveUnknownClassSamePackage() throws IOException {
		
		final CompiledAndResolvedFile referer = compile(refererSpec, refererSource, new TestResolvedTypes());
				
		assertThat(referer).isNotNull();
		assertThat(referer.getErrors().isEmpty()).isFalse();
		
		assertThat(referer.getErrors().size()).isEqualTo(1);
		assertThat(referer.getErrors().iterator().next() instanceof ResolveError).isTrue();
		assertThat(referer.getErrors().iterator().next().getMessage().contains("Refered")).isTrue();
	}
		
	@Test
	public void testResolveOtherClassSamePackage() throws IOException {
						
		final CompiledAndResolvedFile referer = new CompileFileCollector()
				.add(refererSpec, refererSource)
				.add("Refered.java", referedSource)
				.compile(new TestResolvedTypes())
				.getFile(refererSpec);

		assertThat(referer).isNotNull();
		assertThat(referer.getErrors().isEmpty()).isTrue();

		final List<BaseASTElement> elements = referer.getASTElements(BaseASTElement.class);
		final Iterator<BaseASTElement> iter = elements.iterator();

		checkClassHeader(iter);
		
		final ClassDataFieldMember field = get(iter);
		assertThat(field.getNameString()).isEqualTo("refered");
	
		final FieldModifiers fieldModifiers = get(iter);
		assertThat(fieldModifiers.hasModifier(FieldVisibility.class)).isTrue();
		assertThat(fieldModifiers.getModifier(FieldVisibility.class) == FieldVisibility.PRIVATE).isTrue();

		final FieldModifierHolder fieldModifierHolder = get(iter);
		assertThat(fieldModifierHolder.getModifier() instanceof FieldVisibility).isTrue();
		
		final ComplexTypeReference typeReference = get(iter);
		assertThat(typeReference.getType() instanceof NamedType).isTrue();
		final NamedType namedType = (NamedType)typeReference.getType();
		assertThat(namedType.getTypeName().getNamespace()).isEqualTo(new String [] { "com", "test" });
		assertThat(namedType.getTypeName().getOuterTypes()).isNull();
		assertThat(namedType.getTypeName().getName()).isEqualTo("Refered");
		
		final FieldNameDeclaration fieldName = get(iter);
		assertThat(fieldName.getName()).isEqualTo("refered");
	}

	
	private static void checkClassHeader(Iterator<BaseASTElement> iter) {
		final CompilationUnit compilationUnit = get(iter);
		assertThat(compilationUnit).isNotNull();
		
		final Namespace namespace = get(iter);
		assertThat(namespace.getParts()).isEqualTo(new String [] { "com", "test" });
		
		final Keyword keyword = get(iter);
		assertThat(keyword.getText()).isEqualTo("package");
	
		final NamespaceDeclaration namespaceDeclaration = get(iter);
		assertThat(namespaceDeclaration.getNamespaceReference().getParts()).isEqualTo(new String [] { "com", "test" });

		final CompilationCodeLines compilationCodeLines = get(iter);
		assertThat(compilationCodeLines).isNotNull();
		
		final ClassDefinition classDefinition = get(iter);
		assertThat(classDefinition.getNameString()).isEqualTo("Referer");
		
		final ClassModifiers classModifiers = get(iter);
		assertThat(classModifiers.getModifiers().size()).isEqualTo(0);
		
		final Keyword classKeyword = get(iter);
		assertThat(classKeyword.getText()).isEqualTo("class");
		
		final ClassDeclarationName classDeclarationName = get(iter);
		assertThat(classDeclarationName.getNameString()).isEqualTo("Referer");
	}
}
