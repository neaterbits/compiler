package com.neaterbits.compiler.resolver.references;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.neaterbits.compiler.resolver.BaseResolveTest;
import com.neaterbits.compiler.resolver.ReferenceLocation;
import com.neaterbits.compiler.resolver.ResolvedTypeCodeMapImpl;
import com.neaterbits.compiler.resolver.TestResolvedFile;
import com.neaterbits.compiler.resolver.TestResolvedType;
import com.neaterbits.compiler.resolver.ast.objects.model.ObjectProgramModel;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.IntValue;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;
import com.neaterbits.compiler.util.model.ResolveTypesModel;
import com.neaterbits.compiler.util.model.UserDefinedTypeRef;
import com.neaterbits.compiler.util.name.ClassName;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassModifiers;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceDeclarationName;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceModifiers;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceName;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;

public class CodeMapTest extends BaseResolveTest {

	@Test
	public void testClassCodeMap() {

		final ObjectProgramModel astModel = new ObjectProgramModel();

		final List<String> namespace = Arrays.asList("com", "test");
		final String className = "TestClass";

		final ResolvedFile testFile = new TestResolvedFile("TestFile.java");

		final ClassDefinition classDefinition = makeClassType(namespace, className);

		final CompilationUnit compilationUnit = new CompilationUnit(
				Context.makeTestContext(),
				Collections.emptyList(),
				Arrays.asList(classDefinition));
		
		final UserDefinedTypeRef classType = new UserDefinedTypeRef(
				TypeName.fromNamespace(namespace, className),
				testFile.getSpec(),
				compilationUnit.getParseTreeRefFromElement(classDefinition));
		
		final ResolvedTypeCodeMapImpl<CompilationUnit> codeMap
				= new ResolvedTypeCodeMapImpl<>(new IntCompilerCodeMap(), Collections.emptyList(), astModel);
		

		final ScopedName scopedName = new ScopedName(namespace, className);
		
		final ResolvedType resolvedClass = new TestResolvedType(
				testFile.getSpec(),
				scopedName,
				TypeVariant.CLASS,
				classType);

		final int typeNo = codeMap.addType(compilationUnit, resolvedClass);

		final int fileNo = codeMap.addResolvedFile(testFile, new int [] { typeNo });
		assertThat(fileNo).isGreaterThanOrEqualTo(0);

		final Collection<ResolvedType> directSubtypes = codeMap.getDirectExtendingThis(
				resolvedClass.getTypeName()
		);
		
		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.isEmpty());
		
		assertThat(codeMap.getClassThisExtendsFrom(resolvedClass.getTypeName())).isNull();

		final Collection<ResolvedType> allSubtypes = codeMap.getAllSubtypes(
				resolvedClass.getTypeName());
		
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes).isEmpty();
		
	}

	@Test
	public void testInterfaceCodeMap() {

		final ObjectProgramModel astModel = new ObjectProgramModel();

		final ResolvedTypeCodeMapImpl<CompilationUnit> codeMap
				= new ResolvedTypeCodeMapImpl<>(new IntCompilerCodeMap(), Collections.emptyList(), astModel);
		
		final TestResolvedFile testFile = new TestResolvedFile("TestFile.java");

		final List<String> namespace = Arrays.asList("com", "test");
		final String interfaceName = "TestInterface";
		final ScopedName scopedName = new ScopedName(namespace, interfaceName);
		
		final IntValue interfaceTokenSequenceNo = new IntValue(1);
		
		final InterfaceDefinition interfaceDefinition = makeInterfaceType(namespace, interfaceName, interfaceTokenSequenceNo);
		
		final CompilationUnit compilationUnit = new CompilationUnit(
				Context.makeTestContext(),
				Collections.emptyList(),
				Arrays.asList(interfaceDefinition));
		
		final UserDefinedTypeRef interfaceType = new UserDefinedTypeRef(
				TypeName.fromNamespace(namespace, interfaceName),
				testFile.getSpec(),
				compilationUnit.getParseTreeRefFromElement(interfaceDefinition));
		
		final ResolvedType resolvedInterface = new TestResolvedType(
				testFile.getSpec(),
				scopedName,
				TypeVariant.INTERFACE,
				interfaceType);

		final int typeNo = codeMap.addType(compilationUnit, resolvedInterface);

		final int fileNo = codeMap.addResolvedFile(testFile, new int [] { typeNo });
		assertThat(fileNo).isGreaterThanOrEqualTo(0);

		
		final Collection<ResolvedType> directSubtypes = codeMap.getDirectExtendingThis(
					resolvedInterface.getTypeName());

		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.isEmpty());
		
		assertThat(codeMap.getClassThisExtendsFrom(resolvedInterface.getTypeName())).isNull();

		final Collection<ResolvedType> allSubtypes = codeMap.getAllSubtypes(resolvedInterface.getTypeName());
		
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes).isEmpty();
	}

	@Test
	public void testClassImplementingInterface() {
		
		final ObjectProgramModel astModel = new ObjectProgramModel();
		
		final ResolvedTypeCodeMapImpl<CompilationUnit> codeMap
				= new ResolvedTypeCodeMapImpl<>(new IntCompilerCodeMap(), Collections.emptyList(), astModel);
		
		final TestResolvedFile classTestFile = new TestResolvedFile("TestClass.java");

		final TestResolvedFile interfaceTestFile = new TestResolvedFile("TestInterface.java");

		final List<String> classNamespace = Arrays.asList("com", "test");
		final String className = "TestClass";
		final ScopedName classScopedName = new ScopedName(classNamespace, className);

		final ClassDefinition classDefinition = makeClassType(classNamespace, className);
		final CompilationUnit classCompilationUnit = new CompilationUnit(
				Context.makeTestContext(),
				Collections.emptyList(),
				Arrays.asList(classDefinition));
		
		final UserDefinedTypeRef classType = new UserDefinedTypeRef(
				TypeName.fromNamespace(classNamespace, className),
				classTestFile.getSpec(),
				classCompilationUnit.getParseTreeRefFromElement(classDefinition));
		
		final List<String> interfaceNamespace = Arrays.asList("com", "test");
		final String interfaceName = "TestInterface";
		
		final ScopedName interfaceScopedName = new ScopedName(interfaceNamespace, interfaceName);
		
		final IntValue interfaceTokenSequenceNo = new IntValue(1);
		final InterfaceDefinition interfaceDefinition = makeInterfaceType(interfaceNamespace, interfaceName, interfaceTokenSequenceNo);
		
		final CompilationUnit interfaceCompilationUnit = new CompilationUnit(
				Context.makeTestContext(),
				Collections.emptyList(),
				Arrays.asList(interfaceDefinition));
		
		final UserDefinedTypeRef interfaceType = new UserDefinedTypeRef(
				TypeName.fromNamespace(interfaceNamespace, interfaceName),
				interfaceTestFile.getSpec(),
				interfaceCompilationUnit.getParseTreeRefFromElement(interfaceDefinition));
		
		final ResolvedType resolvedInterface = new TestResolvedType(
				interfaceTestFile.getSpec(),
				interfaceScopedName,
				TypeVariant.INTERFACE,
				interfaceType);
		
		final ResolvedTypeDependency interfaceDependency = new ResolvedTypeDependency(
				resolvedInterface.getTypeName(),
				ReferenceLocation.EXTENDS_FROM,
				-1, TypeResolveMode.CLASSNAME_TO_COMPLETE,
				TypeVariant.INTERFACE,
				null, null);
		
		final ResolvedType resolvedClass = new TestResolvedType(
				classTestFile.getSpec(),
				classScopedName,
				TypeVariant.CLASS,
				classType,
				null,
				Arrays.asList(interfaceDependency),
				null);

		final int interfaceTypeNo = codeMap.addType(interfaceCompilationUnit, resolvedInterface);
		
		
		final int classTypeNo = codeMap.addType(classCompilationUnit, resolvedClass);
		
		assertThat(classTypeNo).isEqualTo(interfaceTypeNo + 1);

		final int classFileNo = codeMap.addResolvedFile(classTestFile, new int [] { classTypeNo });
		assertThat(classFileNo).isGreaterThanOrEqualTo(0);
		
		final int interfaceFileNo = codeMap.addResolvedFile(interfaceTestFile, new int [] { interfaceTypeNo });
		assertThat(interfaceFileNo).isEqualTo(classFileNo + 1);

		final List<ResolvedType> directExtendingThis = codeMap.getDirectExtendingThis(resolvedInterface.getTypeName());
		
		assertThat(directExtendingThis).isNotNull();
		assertThat(directExtendingThis.size()).isEqualTo(1);
		assertThat(directExtendingThis.iterator().next()).isSameAs(resolvedClass);

		final List<ResolvedType> allExtendingThis = codeMap.getAllSubtypes(resolvedInterface.getTypeName());
		assertThat(allExtendingThis).isNotNull();
		assertThat(allExtendingThis.size()).isEqualTo(1);
		assertThat(allExtendingThis.iterator().next()).isSameAs(resolvedClass);

		assertThat(codeMap.getClassThisExtendsFrom(resolvedInterface.getTypeName())).isNull();

		final ResolvedType anotherClass
				= addType(astModel, codeMap, "AnotherTestClass.java", "com.test.AnotherTestClass", TypeVariant.CLASS, resolvedClass);
		final ResolvedType anotherInterface
				= addType(astModel, codeMap, "AnotherTestInterface.java", "com.test.AnotherTestInterface", TypeVariant.INTERFACE, resolvedInterface);

		final List<ResolvedType> directSubtypesUpdated = codeMap.getDirectExtendingThis(resolvedInterface.getTypeName());

		assertThat(directSubtypesUpdated).isNotNull();
		assertThat(directSubtypesUpdated.size()).isEqualTo(2);
		assertThat(directSubtypesUpdated.get(0)).isSameAs(resolvedClass);
		assertThat(directSubtypesUpdated.get(1)).isSameAs(anotherInterface);

		final List<ResolvedType> allSubtypesUpdated = codeMap.getAllSubtypes(resolvedInterface.getTypeName());

		assertThat(allSubtypesUpdated).isNotNull();
		
		assertThat(allSubtypesUpdated.size()).isEqualTo(3);
		assertThat(allSubtypesUpdated.get(0)).isSameAs(resolvedClass);
		assertThat(allSubtypesUpdated.get(1)).isSameAs(anotherInterface);
		assertThat(allSubtypesUpdated.get(2)).isSameAs(anotherClass);
	}

	@SafeVarargs
	private final ResolvedType addType(
			ResolveTypesModel<CompilationUnit> astModel,
			ResolvedTypeCodeMapImpl<CompilationUnit> codeMap,
			String file,
			String name,
			TypeVariant typeVariant,
			ResolvedType ... extendsFrom) {
		
		final ResolvedFile resolvedFile = new TestResolvedFile(file);

		final TypeName typeName = makeTypeName(name);

		final ClassDefinition classDefinition = makeClassType(
				Arrays.asList(typeName.getNamespace()),
				typeName.getName());
		
		final CompilationUnit compilationUnit = new CompilationUnit(
				Context.makeTestContext(),
				Collections.emptyList(),
				Arrays.asList(classDefinition));

		final ResolvedType resolvedType = makeResolvedType(
				astModel,
				resolvedFile,
				typeName,
				typeVariant,
				compilationUnit.getParseTreeRefFromElement(classDefinition),
				extendsFrom);
		
		final int typeNo = codeMap.addType(compilationUnit, resolvedType);

		codeMap.addResolvedFile(resolvedFile, new int [] { typeNo });

		return resolvedType;
	}
	
	private static ClassDefinition makeClassType(Collection<String> namespace, String name) {
		
		final Context context = Context.makeTestContext();
		final Context nameContext = Context.makeTestContext();
		
		final ClassDefinition classDefinition = new ClassDefinition(
				context,
				new ClassModifiers(Collections.emptyList()),
				null,
				new ClassDeclarationName(nameContext, new ClassName(name)),
				null,
				null,
				null,
				Collections.emptyList());

		return classDefinition;
	}

	private static InterfaceDefinition makeInterfaceType(Collection<String> namespace, String name, IntValue tokenSequenceNo) {
		
		final Context context = Context.makeTestContext();
		final Context nameContext = Context.makeTestContext();
		
		final InterfaceDefinition interfaceDefinition = new InterfaceDefinition(
				context,
				new InterfaceModifiers(Collections.emptyList()),
				null,
				new InterfaceDeclarationName(nameContext, new InterfaceName(name)),
				null,
				Collections.emptyList());

		return interfaceDefinition;
	}
}
