package dev.nimbler.compiler.language.java.resolve;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.CompilationCodeLines;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.Namespace;
import dev.nimbler.compiler.ast.objects.NamespaceDeclaration;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldModifiers;
import dev.nimbler.compiler.ast.objects.typereference.ComplexTypeReference;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.ast.objects.variables.VarNameDeclaration;
import dev.nimbler.compiler.language.java.compile.BaseCompilerTest;
import dev.nimbler.compiler.language.java.compile.CompileFileCollector;
import dev.nimbler.compiler.language.java.compile.CompiledAndResolvedFile;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.types.typedefinition.FieldVisibility;
import dev.nimbler.language.common.types.Visibility;

public class ResolveSamePackageTest extends BaseCompilerTest {

	final String refererSource =
			
			"package com.test;\n"
		+   "class Referer {\n"
		+   " private Refered refered;\n"
		+   "}\n";

	final String refererName = "Referer.java";
	
	final String referedSource =
			
			"package com.test;\n"
		+   "class Refered {\n"
		+   "}\n";

	@Test
	public void testResolveUnknownClassSamePackage() throws IOException, ParserException {
		
		final CompiledAndResolvedFile referer = compile(refererName, refererSource, new TestResolvedTypes());
				
		assertThat(referer).isNotNull();
		assertThat(referer.getErrors().isEmpty()).isFalse();
		
		assertThat(referer.getErrors().size()).isEqualTo(1);
		assertThat(referer.getErrors().iterator().next() instanceof ResolveError).isTrue();
		
		System.out.println("## message '" + referer.getErrors().iterator().next().getMessage() + "'");
		
		assertThat(referer.getErrors().iterator().next().getMessage().contains("Refered")).isTrue();
	}
		
	@Test
	public void testResolveOtherClassSamePackage() throws IOException, ParserException {
						
		final CompiledAndResolvedFile referer = new CompileFileCollector<>(this::compileFiles)
				.add(refererName, refererSource)
				.add("Refered.java", referedSource)
				.compile(new TestResolvedTypes())
				.getFile(refererName);

		assertThat(referer).isNotNull();
		assertThat(referer.getErrors().isEmpty()).isTrue();

		final List<BaseASTElement> elements = referer.getASTElements(BaseASTElement.class);
		final Iterator<BaseASTElement> iter = elements.iterator();

		checkClassHeader(iter);
		
		final ClassDataFieldMember field = get(iter);
		assertThat(field.getInitializer(0).getNameString()).isEqualTo("refered");
	
		final FieldModifiers fieldModifiers = get(iter);
		assertThat(fieldModifiers.hasModifier(FieldVisibility.class)).isTrue();
		assertThat(fieldModifiers.getModifier(FieldVisibility.class).getVisibility()).isEqualTo(Visibility.PRIVATE);

		final FieldModifierHolder fieldModifierHolder = get(iter);
		assertThat(fieldModifierHolder.getModifier() instanceof FieldVisibility).isTrue();
		
		final ComplexTypeReference typeReference = get(iter);
		
		assertThat(typeReference.getTypeName().getNamespace()).isEqualTo(new String [] { "com", "test" });
		assertThat(typeReference.getTypeName().getOuterTypes()).isNull();
		assertThat(typeReference.getTypeName().getName()).isEqualTo("Refered");
		
		final InitializerVariableDeclarationElement initializerVariableDeclarationElement = get(iter);
		assertThat(initializerVariableDeclarationElement.getNumDims()).isEqualTo(0);
        assertThat(initializerVariableDeclarationElement.getInitializer()).isNull();
		
		final VarNameDeclaration fieldName = get(iter);
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
