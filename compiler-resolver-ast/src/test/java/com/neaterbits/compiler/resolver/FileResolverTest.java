package com.neaterbits.compiler.resolver;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
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
import com.neaterbits.compiler.resolver.references.TestResolvedTypeDependency;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.NameFileSpec;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.NamespaceReference;
import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassName;
import com.neaterbits.compiler.codemap.TypeVariant;

public class FileResolverTest extends BaseResolveTest {

	@Test
	public void testResolve2Classes() {
		
		final PrintStream loggerStream = new PrintStream(new ByteArrayOutputStream());
		
		final ResolveLogger<BuiltinType, ComplexType<?, ?, ?>, TypeName, CompilationUnit> resolveLogger = new ResolveLogger<>(loggerStream);

		final ASTModelImpl astModel = new ASTModelImpl() {

			@Override
			public ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName> makeResolvedTypeDependency(
					TypeName completeName, ReferenceType referenceType, TypeResolveMode typeResolveMode,
					TypeVariant typeVariant, CompiledTypeDependency compiledTypeDependency) {

				return new TestResolvedTypeDependency(completeName, referenceType, typeVariant);
			}
		};
		
		final FilesResolver<BuiltinType, ComplexType<?, ?, ?>, TypeName, CompilationUnit> filesResolver
			= new FilesResolver<>(resolveLogger, Collections.emptyList(), null, new ObjectProgramModel(), astModel);

		final FileSpec testFileSpec = new NameFileSpec("TestClass.java");
		final TypeName testClass = makeTypeName("com.test.TestClass");
		final ClassType testClassType = makeClassType(testClass);
		final CompiledType<ComplexType<?, ?, ?>> testType = new TestCompiledType(
				testFileSpec,
				testClass.toScopedName(),
				TypeVariant.CLASS,
				testClassType,
				null, 
				null, 
				null);
		
		final Context context = new Context("", 0, 0, 0, 0, 0, 0, "");
		
		final CompilationUnit compilationUnit = new CompilationUnit(context, new ArrayList<>(), new ArrayList<>());
		
		final TestCompiledFile testFile = new TestCompiledFile(testFileSpec, compilationUnit, testType);

		final FileSpec anotherTestFileSpec = new NameFileSpec("AnotherTestClass.java");
		final TypeName anotherTestClass = makeTypeName("com.test.AnotherTestClass");
		
		final ClassType anotherTestClassType = makeClassType(anotherTestClass);
		
		final CompiledType<ComplexType<?, ?, ?>> anotherTestType = new TestCompiledType(
				anotherTestFileSpec,
				anotherTestClass.toScopedName(),
				TypeVariant.CLASS,
				anotherTestClassType,
				null,
				Arrays.asList(makeExtendsFromDependency(testClass.toScopedName())),
				null);

		final TestCompiledFile anotherTestFile = new TestCompiledFile(anotherTestFileSpec, compilationUnit, anotherTestType);
		
		final List<CompiledFile<ComplexType<?, ?, ?>, CompilationUnit>> compiledFiles = Arrays.asList(
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
	
	private static ClassType makeClassType(TypeName anotherTestClass) {
		
		final NamespaceReference namespaceReference = new NamespaceReference(Arrays.asList(anotherTestClass.getNamespace()));
		
		final ClassType anotherTestClassType = new ClassType(
				namespaceReference,
				null,
				new ClassDefinition(
						Context.makeTestContext(),
						new ClassModifiers(Collections.emptyList()),
						new Keyword(Context.makeTestContext(), "class"),
						new ClassDeclarationName(Context.makeTestContext(), new ClassName(anotherTestClass.getName())),
						null,
						null,
						null,
						Collections.emptyList()));
		
		return anotherTestClassType;
	}
	
	private static CompiledTypeDependency makeExtendsFromDependency(ScopedName scopedName) {
		return new TestCompiledTypeDependency(scopedName, TypeVariant.CLASS, ReferenceType.EXTENDS_FROM);
	}
}
