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
import com.neaterbits.compiler.resolver.ast.model.ObjectProgramModel;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassName;
import com.neaterbits.compiler.codemap.TypeVariant;

public class FileResolverTest extends BaseResolveTest {

	@Test
	public void testResolve2Classes() {
		
		final PrintStream loggerStream = new PrintStream(new ByteArrayOutputStream());
		
		final ResolveLogger<CompilationUnit> resolveLogger = new ResolveLogger<>(loggerStream);

		final ASTModelImpl astModel = new ASTModelImpl();
		
		final FilesResolver<CompilationUnit> filesResolver
			= new FilesResolver<>(resolveLogger, Collections.emptyList(), null, new ObjectProgramModel(null), astModel);

		final FileSpec testFileSpec = new NameFileSpec("TestClass.java");
		final TypeName testClass = makeTypeName("com.test.TestClass");
		final ClassDefinition testClassDefinition = makeClassType(testClass);
		
		final CompilationUnit testCompilationUnit = new CompilationUnit(
				Context.makeTestContext(),
				Collections.emptyList(),
				Arrays.asList(testClassDefinition));
		
		final UserDefinedTypeRef testClassType = new UserDefinedTypeRef(
				testClass,
				testFileSpec,
				testCompilationUnit.getParseTreeRefFromElement(testClassDefinition));
		
		final CompiledType testType = new TestCompiledType(
				testFileSpec,
				testClass.toScopedName(),
				TypeVariant.CLASS,
				testClassType,
				null, 
				null, 
				null);
		
		final Context context = Context.makeTestContext();
		
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

		final CompiledType anotherTestType = new TestCompiledType(
				anotherTestFileSpec,
				anotherTestClass.toScopedName(),
				TypeVariant.CLASS,
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
						Context.makeTestContext(),
						new ClassModifiers(Collections.emptyList()),
						new Keyword(Context.makeTestContext(), "class"),
						new ClassDeclarationName(Context.makeTestContext(), new ClassName(anotherTestClass.getName())),
						null,
						null,
						null,
						Collections.emptyList());
	}
	
	private static CompiledTypeDependency makeExtendsFromDependency(ScopedName scopedName) {
		return new CompiledTypeDependency(scopedName, ReferenceType.EXTENDS_FROM, -1, null, null);
	}
}
