package com.neaterbits.compiler.resolver;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.neaterbits.compiler.resolver.FilesResolver;
import com.neaterbits.compiler.resolver.ReferenceLocation;
import com.neaterbits.compiler.resolver.ResolveFilesResult;
import com.neaterbits.compiler.resolver.ResolveLogger;
import com.neaterbits.compiler.resolver.ast.objects.model.ObjectProgramModel;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.FullContext;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;
import com.neaterbits.compiler.util.name.ClassName;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassModifiers;
import com.neaterbits.compiler.codemap.TypeVariant;

public class FileResolverTest extends BaseResolveTest {

	@Test
	public void testResolve2Classes() {
		
		final PrintStream loggerStream = new PrintStream(new ByteArrayOutputStream());
		
		final ResolveLogger<CompilationUnit> resolveLogger = new ResolveLogger<>(loggerStream);

		final ObjectProgramModel astModel = new ObjectProgramModel();
		
		final FilesResolver<CompilationUnit> filesResolver
			= new FilesResolver<>(resolveLogger, Collections.emptyList(), null, astModel, astModel);

		final FileSpec testFileSpec = new NameFileSpec("TestClass.java");
		final TypeName testClass = makeTypeName("com.test.TestClass");
		
		final ClassDefinition testClassDefinition = makeClassType(testClass);
		
		final CompilationUnit testCompilationUnit = new CompilationUnit(
				FullContext.makeTestContext(),
				Collections.emptyList(),
				Arrays.asList(testClassDefinition));
		
		final UserDefinedTypeRef testClassType = new UserDefinedTypeRef(
				testClass,
				testFileSpec,
				testCompilationUnit.getParseTreeRefFromElement(testClassDefinition));
		
		final CompiledType testType = new CompiledType(
				testFileSpec,
				new TypeSpec(testClass.toScopedName(), TypeVariant.CLASS),
				testClassType,
				null, 
				null, 
				null);
		
		
		final Context context = FullContext.makeTestContext();
		
		final FileSpec anotherTestFileSpec = new NameFileSpec("AnotherTestClass.java");
		final TypeName anotherTestClass = makeTypeName("com.test.AnotherTestClass");
		
		final ClassDefinition anotherTestClassDefinition = makeClassType(anotherTestClass);

		final CompilationUnit anotherCompilationUnit = new CompilationUnit(
				context,
				Collections.emptyList(),
				Arrays.asList(anotherTestClassDefinition));

		final CompiledFile<CompilationUnit> testFile = new CompiledFile<>(testFileSpec, anotherCompilationUnit, testType);
		
		final UserDefinedTypeRef anotherTestClassType = new UserDefinedTypeRef(
				anotherTestClass,
				anotherTestFileSpec,
				anotherCompilationUnit.getParseTreeRefFromElement(anotherTestClassDefinition));

		final CompiledType anotherTestType = new CompiledType(
				anotherTestFileSpec,
				new TypeSpec(anotherTestClass.toScopedName(), TypeVariant.CLASS),
				anotherTestClassType,
				null,
				Arrays.asList(makeExtendsFromDependency(testClass.toScopedName())),
				null);

		final CompiledFile<CompilationUnit> anotherTestFile = new CompiledFile<>(anotherTestFileSpec, anotherCompilationUnit, anotherTestType);
		
		final List<CompiledFile<CompilationUnit>> compiledFiles = Arrays.asList(
				testFile,
				anotherTestFile
		);

		final ResolveFilesResult result = filesResolver.resolveFiles(compiledFiles);
		
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
	
	private static ClassDefinition makeClassType(TypeName anotherTestClass) {
		
		return new ClassDefinition(
						FullContext.makeTestContext(),
						new ClassModifiers(null, Collections.emptyList()),
						new Keyword(FullContext.makeTestContext(), "class"),
						new ClassDeclarationName(FullContext.makeTestContext(), new ClassName(anotherTestClass.getName())),
						null,
						null,
						null,
						null,
						Collections.emptyList());
	}
	
	private static CompiledTypeDependency makeExtendsFromDependency(ScopedName scopedName) {
		return new CompiledTypeDependency(scopedName, ReferenceLocation.EXTENDS_FROM, -1, null, null);
	}
}
