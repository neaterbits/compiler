package com.neaterbits.compiler.resolver;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.neaterbits.compiler.resolver.FilesResolver;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.resolver.ResolveLogger;
import com.neaterbits.compiler.resolver.ast.ASTModelImpl;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.codemap.TypeVariant;

public class FileResolverTest extends BaseResolveTest {

	@Test
	public void testResolve2Classes() {
		
		final PrintStream loggerStream = new PrintStream(new ByteArrayOutputStream());
		
		final ResolveLogger<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolveLogger = new ResolveLogger<>(loggerStream);

		final ASTModelImpl astModel = new ASTModelImpl();
		
		final FilesResolver<BuiltinType, ComplexType<?, ?, ?>, TypeName> filesResolver
			= new FilesResolver<>(resolveLogger, Collections.emptyList(), null, astModel);

		final FileSpec testFileSpec = new TestFileSpec("TestClass.java");
		final ScopedName testClass = makeScopedName("com.test.TestClass");
		final CompiledType<ComplexType<?, ?, ?>> testType = new TestCompiledType(testFileSpec, testClass, TypeVariant.CLASS, null, null, null, null);
		final TestCompiledFile testFile = new TestCompiledFile(testFileSpec, new TestFileImports(), testType);

		final FileSpec anotherTestFileSpec = new TestFileSpec("AnotherTestClass.java");
		final ScopedName anotherTestClass = makeScopedName("com.test.AnotherTestClass");
		final CompiledType<ComplexType<?, ?, ?>> anotherTestType = new TestCompiledType(anotherTestFileSpec, anotherTestClass, TypeVariant.CLASS, null,
					null,
					Arrays.asList(makeExtendsFromDependency(testClass)),
					null);

		final TestCompiledFile anotherTestFile = new TestCompiledFile(anotherTestFileSpec, new TestFileImports(), anotherTestType);
		
		final List<CompiledFile<ComplexType<?, ?, ?>>> compiledFiles = Arrays.asList(
				testFile,
				anotherTestFile
		);

		final ResolveFilesResult<BuiltinType, ComplexType<?, ?, ?>, TypeName> result = filesResolver.resolveFiles(compiledFiles);
		
		assertThat(result).isNotNull();
		
		assertThat(result.getType(testType.getTypeName())).isNotNull();
		assertThat(result.getType(anotherTestType.getTypeName())).isNotNull();
		
		/*
		assertThat(result.getUnresolvedExtendsFrom(testFileSpec)).isEmpty();
		assertThat(result.getUnresolvedTypeDependencies(testFileSpec)).isEmpty();

		assertThat(result.getUnresolvedExtendsFrom(anotherTestFileSpec)).isEmpty();
		assertThat(result.getUnresolvedTypeDependencies(anotherTestFileSpec)).isEmpty();
		*/
	}
	
	private static CompiledTypeDependency makeExtendsFromDependency(ScopedName scopedName) {
		return new TestCompiledTypeDependency(scopedName, TypeVariant.CLASS, ReferenceType.EXTENDS_FROM);
	}
}
