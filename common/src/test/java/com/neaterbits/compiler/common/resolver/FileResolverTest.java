package com.neaterbits.compiler.common.resolver;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.TypeVariant;

public class FileResolverTest extends BaseResolveTest {

	@Test
	public void testResolveClasses() {
		
		final PrintStream loggerStream = new PrintStream(new ByteArrayOutputStream());
		
		final ResolveLogger resolveLogger = new ResolveLogger(loggerStream);

		final FilesResolver filesResolver = new FilesResolver(resolveLogger);

		final FileSpec testFileSpec = new TestFileSpec("TestClass.java");
		final ScopedName testClass = makeScopedName("com.test.TestClass");
		final CompiledType testType = new TestCompiledType(testFileSpec, testClass, TypeVariant.CLASS, null, null, null, null);
		final TestCompiledFile testFile = new TestCompiledFile(testFileSpec, new TestFileImports(), testType);

		final FileSpec anotherTestFileSpec = new TestFileSpec("AnotherTestClass.java");
		final ScopedName anotherTestClass = makeScopedName("com.test.AnotherTestClass");
		final CompiledType anotherTestType = new TestCompiledType(anotherTestFileSpec, anotherTestClass, TypeVariant.CLASS, null,
					null,
					Arrays.asList(makeExtendsFromDependency(testClass)),
					null);

		final TestCompiledFile anotherTestFile = new TestCompiledFile(anotherTestFileSpec, new TestFileImports(), anotherTestType);
		
		final List<CompiledFile> compiledFiles = Arrays.asList(
				testFile,
				anotherTestFile
		);

		final ResolveFilesResult result = filesResolver.resolveFiles(compiledFiles);
		
		assertThat(result).isNotNull();
		
		assertThat(result.getType(testClass)).isNotNull();
		assertThat(result.getType(anotherTestClass)).isNotNull();
		
		assertThat(result.getUnresolvedExtendsFrom(testFileSpec)).isEmpty();
		assertThat(result.getUnresolvedTypeDependencies(testFileSpec)).isEmpty();

		assertThat(result.getUnresolvedExtendsFrom(anotherTestFileSpec)).isEmpty();
		assertThat(result.getUnresolvedTypeDependencies(anotherTestFileSpec)).isEmpty();
	}
	
	private static CompiledTypeDependency makeExtendsFromDependency(ScopedName scopedName) {
		return new TestCompiledTypeDependency(scopedName, TypeVariant.CLASS, ReferenceType.EXTENDS_FROM);
	}
}
