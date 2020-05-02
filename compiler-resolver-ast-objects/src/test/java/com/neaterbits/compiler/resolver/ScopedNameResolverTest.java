package com.neaterbits.compiler.resolver;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Import;
import com.neaterbits.compiler.ast.objects.ImportName;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassOrInterfaceName;
import com.neaterbits.compiler.resolver.ast.objects.model.ObjectImportsModel;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.name.NamespaceReference;

public class ScopedNameResolverTest {

	private final ObjectImportsModel importsModel = new ObjectImportsModel(null);
	
	
	@Test
	public void testClassInSameTypesMapNamespace() {

		final TestTypesMap typesMap = new TestTypesMap("com.test.InSameNamespace");
		
		final String scopedName = ScopedNameResolver.resolveScopedName(
				ScopedName.makeScopedName(new String [] { "com", "test" }, "InSameNamespace"),
				ReferenceLocation.FIELD,
				makeCompilationUnit(),
				importsModel,
				ScopedName.makeScopedName(new String [] { "com", "test" }, "ClassReferenceFrom"),
				typesMap);

		assertThat(scopedName).isEqualTo("com.test.InSameNamespace");
		
	}

	@Test
	public void testClassInOtherTypesMapNamespace() {

		final TestTypesMap typesMap = new TestTypesMap("com.test.othernamespace.InOtherNamespace");
		
		final String scopedName = ScopedNameResolver.resolveScopedName(
				ScopedName.makeScopedName(new String [] { "com", "test", "othernamespace" }, "InOtherNamespace"),
				ReferenceLocation.FIELD,
				makeCompilationUnit(),
				importsModel,
				ScopedName.makeScopedName(new String [] { "com", "test" }, "ClassReferenceFrom"),
				typesMap);

		assertThat(scopedName).isEqualTo("com.test.othernamespace.InOtherNamespace");
	}

	@Test
	public void testClassInImportedNamespace() {

		final TestTypesMap typesMap = new TestTypesMap("com.test.importnamespace.InImportedNamespace");
		
		final NamespaceReference namespaceReference = new NamespaceReference(new String [] { "com", "test", "importnamespace" });
		
		final Import importStatement = new Import(
				Context.makeTestContext(),
				new Keyword(Context.makeTestContext(), "testtoken"),
				new ImportName(
					Context.makeTestContext(),
					namespaceReference,
					new ClassOrInterfaceName("InImportedNamespace")));
		
		final String scopedName = ScopedNameResolver.resolveScopedName(
				ScopedName.makeScopedName(new String [] { "InImportedNamespace" }),
				ReferenceLocation.FIELD,
				makeCompilationUnit(importStatement),
				importsModel,
				ScopedName.makeScopedName(new String [] { "com", "test" }, "ClassReferenceFrom"),
				typesMap);

		assertThat(scopedName).isEqualTo("com.test.importnamespace.InImportedNamespace");
	}

	@Test
	public void testClassNotInImportedNamespace() {

		final TestTypesMap typesMap = new TestTypesMap("com.test.importnamespace.OtherClassInImportedNamespace");
		
		final NamespaceReference namespaceReference = new NamespaceReference(new String [] { "com", "test", "importnamespace" });
		
		final Import importStatement = new Import(
				Context.makeTestContext(),
				new Keyword(Context.makeTestContext(), "import"),
				new ImportName(
						Context.makeTestContext(),
						namespaceReference,
						new ClassOrInterfaceName("InImportedNamespace")));
		
		final String scopedName = ScopedNameResolver.resolveScopedName(
				ScopedName.makeScopedName(new String [] { "InImportedNamespace" }),
				ReferenceLocation.FIELD,
				makeCompilationUnit(importStatement),
				importsModel,
				ScopedName.makeScopedName(new String [] { "com", "test" }, "ClassReferenceFrom"),
				typesMap);

		assertThat(scopedName).isNull();
	}

	private static CompilationUnit makeCompilationUnit(Import ... imports) {

		final Context context = Context.makeTestContext();
		
		final CompilationUnit compilationUnit = new CompilationUnit(context, Arrays.asList(imports), new ArrayList<>());
		
		return compilationUnit;
	}
}
