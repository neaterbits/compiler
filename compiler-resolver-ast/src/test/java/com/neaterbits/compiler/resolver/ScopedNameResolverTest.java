package com.neaterbits.compiler.resolver;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Import;
import com.neaterbits.compiler.ast.ImportName;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.NamespaceReference;
import com.neaterbits.compiler.ast.typedefinition.ClassOrInterfaceName;
import com.neaterbits.compiler.resolver.ast.model.ObjectImportsModel;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.IntValue;
import com.neaterbits.compiler.util.ScopedName;

public class ScopedNameResolverTest {

	private final ObjectImportsModel importsModel = new ObjectImportsModel(null);
	
	
	@Test
	public void testClassInSameTypesMapNamespace() {

		final TestTypesMap typesMap = new TestTypesMap("com.test.InSameNamespace");
		
		final String scopedName = ScopedNameResolver.resolveScopedName(
				ScopedName.makeScopedName(new String [] { "com", "test" }, "InSameNamespace"),
				ReferenceLocation.FIELD,
				makeCompilationUnit(new IntValue(1)),
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
				makeCompilationUnit(new IntValue(1)),
				importsModel,
				ScopedName.makeScopedName(new String [] { "com", "test" }, "ClassReferenceFrom"),
				typesMap);

		assertThat(scopedName).isEqualTo("com.test.othernamespace.InOtherNamespace");
	}

	@Test
	public void testClassInImportedNamespace() {

		final TestTypesMap typesMap = new TestTypesMap("com.test.importnamespace.InImportedNamespace");
		
		final NamespaceReference namespaceReference = new NamespaceReference(new String [] { "com", "test", "importnamespace" });
		
		final IntValue tokenSequenceNo = new IntValue(1);
		
		final Import importStatement = new Import(
				Context.makeTestContext(tokenSequenceNo.increment()),
				new Keyword(Context.makeTestContext(tokenSequenceNo.increment()), "testtoken"),
				new ImportName(
					Context.makeTestContext(tokenSequenceNo.increment()),
					namespaceReference,
					new ClassOrInterfaceName("InImportedNamespace")));
		
		final String scopedName = ScopedNameResolver.resolveScopedName(
				ScopedName.makeScopedName(new String [] { "InImportedNamespace" }),
				ReferenceLocation.FIELD,
				makeCompilationUnit(tokenSequenceNo, importStatement),
				importsModel,
				ScopedName.makeScopedName(new String [] { "com", "test" }, "ClassReferenceFrom"),
				typesMap);

		assertThat(scopedName).isEqualTo("com.test.importnamespace.InImportedNamespace");
	}

	@Test
	public void testClassNotInImportedNamespace() {

		final TestTypesMap typesMap = new TestTypesMap("com.test.importnamespace.OtherClassInImportedNamespace");
		
		final NamespaceReference namespaceReference = new NamespaceReference(new String [] { "com", "test", "importnamespace" });
		
		final IntValue tokenSequenceNo = new IntValue(1);
		
		final Import importStatement = new Import(
				Context.makeTestContext(tokenSequenceNo.increment()),
				new Keyword(Context.makeTestContext(tokenSequenceNo.increment()), "import"),
				new ImportName(
						Context.makeTestContext(tokenSequenceNo.increment()),
						namespaceReference,
						new ClassOrInterfaceName("InImportedNamespace")));
		
		final String scopedName = ScopedNameResolver.resolveScopedName(
				ScopedName.makeScopedName(new String [] { "InImportedNamespace" }),
				ReferenceLocation.FIELD,
				makeCompilationUnit(tokenSequenceNo, importStatement),
				importsModel,
				ScopedName.makeScopedName(new String [] { "com", "test" }, "ClassReferenceFrom"),
				typesMap);

		assertThat(scopedName).isNull();
	}

	private static CompilationUnit makeCompilationUnit(IntValue tokenSequenceNo, Import ... imports) {

		final Context context = Context.makeTestContext(tokenSequenceNo.increment());
		
		final CompilationUnit compilationUnit = new CompilationUnit(context, Arrays.asList(imports), new ArrayList<>());
		
		return compilationUnit;
	}
}
