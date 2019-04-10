package com.neaterbits.compiler.resolver.references;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.neaterbits.compiler.resolver.ASTTypesModel;
import com.neaterbits.compiler.resolver.BaseResolveTest;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.ResolvedCodeMapImpl;
import com.neaterbits.compiler.resolver.ResolvedTypeCodeMapImpl;
import com.neaterbits.compiler.resolver.TestResolvedFile;
import com.neaterbits.compiler.resolver.TestResolvedType;
import com.neaterbits.compiler.resolver.ast.ASTModelImpl;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.ast.NamespaceReference;
import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassName;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.ast.typedefinition.InterfaceModifiers;
import com.neaterbits.compiler.ast.typedefinition.InterfaceName;
import com.neaterbits.compiler.codemap.TypeVariant;

public class CodeMapTest extends BaseResolveTest {

	@Test
	public void testClassCodeMap() {

		final ASTModelImpl astModel = new ASTModelImpl();
		
		final ResolvedTypeCodeMapImpl<BuiltinType, ComplexType<?, ?, ?>, TypeName> codeMap
				= new ResolvedTypeCodeMapImpl<>(new ResolvedCodeMapImpl<>(), Collections.emptyList(), astModel);
		
		final TestResolvedFile testFile = new TestResolvedFile("TestFile.java");

		final List<String> namespace = Arrays.asList("com", "test");
		final String className = "TestClass";
		final ScopedName scopedName = new ScopedName(namespace, className);
		
		final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolvedClass = new TestResolvedType(
				testFile.getSpec(),
				scopedName,
				TypeVariant.CLASS,
				makeClassType(namespace, className));

		final int typeNo = codeMap.addType(resolvedClass);

		final int fileNo = codeMap.addFile(testFile, new int [] { typeNo });
		assertThat(fileNo).isGreaterThanOrEqualTo(0);

		final Collection<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> directSubtypes = codeMap.getDirectSubtypes(
				resolvedClass.getTypeName()
		);
		
		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.isEmpty());
		
		assertThat(codeMap.getClassThisExtendsFrom(resolvedClass.getTypeName())).isNull();

		final Collection<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> allSubtypes = codeMap.getAllSubtypes(
				resolvedClass.getTypeName());
		
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes).isEmpty();
		
	}

	@Test
	public void testInterfaceCodeMap() {

		final ASTModelImpl astModel = new ASTModelImpl();

		final ResolvedTypeCodeMapImpl<BuiltinType, ComplexType<?, ?, ?>, TypeName> codeMap
				= new ResolvedTypeCodeMapImpl<>(new ResolvedCodeMapImpl<>(), Collections.emptyList(), astModel);
		
		final TestResolvedFile testFile = new TestResolvedFile("TestFile.java");

		final List<String> namespace = Arrays.asList("com", "test");
		final String interfaceName = "TestInterface";
		final ScopedName scopedName = new ScopedName(namespace, interfaceName);
		
		final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolvedInterface = new TestResolvedType(
				testFile.getSpec(),
				scopedName,
				TypeVariant.INTERFACE,
				makeInterfaceType(namespace, interfaceName));

		final int typeNo = codeMap.addType(resolvedInterface);

		final int fileNo = codeMap.addFile(testFile, new int [] { typeNo });
		assertThat(fileNo).isGreaterThanOrEqualTo(0);

		
		final Collection<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> directSubtypes = codeMap.getDirectSubtypes(
					resolvedInterface.getTypeName());

		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.isEmpty());
		
		assertThat(codeMap.getClassThisExtendsFrom(resolvedInterface.getTypeName())).isNull();

		final Collection<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> allSubtypes = codeMap.getAllSubtypes(resolvedInterface.getTypeName());
		
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes).isEmpty();
	}

	@Test
	public void testClassImplementingInterface() {
		
		final ASTModelImpl astModel = new ASTModelImpl();
		
		final ResolvedTypeCodeMapImpl<BuiltinType, ComplexType<?, ?, ?>, TypeName> codeMap
				= new ResolvedTypeCodeMapImpl<>(new ResolvedCodeMapImpl<>(), Collections.emptyList(), astModel);
		
		final TestResolvedFile classTestFile = new TestResolvedFile("TestClass.java");

		final TestResolvedFile interfaceTestFile = new TestResolvedFile("TestInterface.java");

		final List<String> classNamespace = Arrays.asList("com", "test");
		final String className = "TestClass";
		final ScopedName classScopedName = new ScopedName(classNamespace, className);
		final ClassType classType = makeClassType(classNamespace, className);
		
		final List<String> interfaceNamespace = Arrays.asList("com", "test");
		final String interfaceName = "TestInterface";
		
		final ScopedName interfaceScopedName = new ScopedName(interfaceNamespace, interfaceName);
		
		final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolvedInterface = new TestResolvedType(
				interfaceTestFile.getSpec(),
				interfaceScopedName,
				TypeVariant.INTERFACE,
				makeInterfaceType(interfaceNamespace, interfaceName));
		
		final ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>, TypeName> interfaceDependency = new TestResolvedTypeDependency(
				classType.getCompleteName().toTypeName(),
				ReferenceType.EXTENDS_FROM,
				TypeVariant.INTERFACE);
		
		final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolvedClass = new TestResolvedType(
				classTestFile.getSpec(),
				classScopedName,
				TypeVariant.CLASS,
				classType,
				null,
				Arrays.asList(interfaceDependency),
				null);

		final int interfaceTypeNo = codeMap.addType(resolvedInterface);
		final int classTypeNo = codeMap.addType(resolvedClass);
		
		assertThat(classTypeNo).isEqualTo(interfaceTypeNo + 1);

		final int classFileNo = codeMap.addFile(classTestFile, new int [] { classTypeNo });
		assertThat(classFileNo).isGreaterThanOrEqualTo(0);
		
		final int interfaceFileNo = codeMap.addFile(interfaceTestFile, new int [] { interfaceTypeNo });
		assertThat(interfaceFileNo).isEqualTo(classFileNo + 1);

		final List<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> directSubtypes = codeMap.getDirectSubtypes(resolvedInterface.getTypeName());
		
		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.size()).isEqualTo(1);
		assertThat(directSubtypes.iterator().next()).isSameAs(resolvedClass);

		final List<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> allSubtypes = codeMap.getAllSubtypes(resolvedInterface.getTypeName());
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes.size()).isEqualTo(1);
		assertThat(allSubtypes.iterator().next()).isSameAs(resolvedClass);

		assertThat(codeMap.getClassThisExtendsFrom(resolvedInterface.getTypeName())).isNull();

		final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> anotherClass
				= addType(astModel, codeMap, "AnotherTestClass.java", "com.test.AnotherTestClass", TypeVariant.CLASS, resolvedClass);
		final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> anotherInterface
				= addType(astModel, codeMap, "AnotherTestInterface.java", "com.test.AnotherTestInterface", TypeVariant.INTERFACE, resolvedInterface);

		final List<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> directSubtypesUpdated = codeMap.getDirectSubtypes(resolvedInterface.getTypeName());

		assertThat(directSubtypesUpdated).isNotNull();
		assertThat(directSubtypesUpdated.size()).isEqualTo(2);
		assertThat(directSubtypesUpdated.get(0)).isSameAs(resolvedClass);
		assertThat(directSubtypesUpdated.get(1)).isSameAs(anotherInterface);

		final List<ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName>> allSubtypesUpdated = codeMap.getAllSubtypes(resolvedInterface.getTypeName());

		assertThat(allSubtypesUpdated).isNotNull();
		
		System.out.println("All subtypes: " + allSubtypesUpdated);
		
		assertThat(allSubtypesUpdated.size()).isEqualTo(3);
		assertThat(allSubtypesUpdated.get(0)).isSameAs(resolvedClass);
		assertThat(allSubtypesUpdated.get(1)).isSameAs(anotherInterface);
		assertThat(allSubtypesUpdated.get(2)).isSameAs(anotherClass);
	}

	@SafeVarargs
	private final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> addType(
			ASTTypesModel<BuiltinType, ComplexType<?, ?, ?>, TypeName> astModel,
			ResolvedTypeCodeMapImpl<BuiltinType, ComplexType<?, ?, ?>, TypeName> codeMap,
			String file,
			String name,
			TypeVariant typeVariant,
			ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> ... extendsFrom) {
		
		final ResolvedFile<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolvedFile = new TestResolvedFile(file);

		final ResolvedType<BuiltinType, ComplexType<?, ?, ?>, TypeName> resolvedType = makeResolvedType(astModel, resolvedFile, name, typeVariant, extendsFrom);

		final int typeNo = codeMap.addType(resolvedType);

		codeMap.addFile(resolvedFile, new int [] { typeNo });

		return resolvedType;
	}
	
	private static ClassType makeClassType(Collection<String> namespace, String name) {
		
		final Context context = new Context("file", 0, 0, 0, 0, 0, 0, "");
		final Context nameContext = new Context("file", 0, 0, 0, 0, 0, 0, "");
		
		final ClassDefinition classDefinition = new ClassDefinition(
				context,
				new ClassModifiers(Collections.emptyList()),
				null,
				new ClassDeclarationName(nameContext, new ClassName(name)),
				null,
				null,
				Collections.emptyList());
		
		return new ClassType(
				new NamespaceReference(namespace),
				null,
				classDefinition);
	}

	private static InterfaceType makeInterfaceType(Collection<String> namespace, String name) {
		
		final Context context = new Context("file", 0, 0, 0, 0, 0, 0, "");
		final Context nameContext = new Context("file", 0, 0, 0, 0, 0, 0, "");
		
		final InterfaceDefinition interfaceDefinition = new InterfaceDefinition(
				context,
				new InterfaceModifiers(Collections.emptyList()),
				null,
				new InterfaceDeclarationName(nameContext, new InterfaceName(name)),
				null,
				Collections.emptyList());
		
		return new InterfaceType(
				new NamespaceReference(namespace),
				null,
				interfaceDefinition);
	}
}
