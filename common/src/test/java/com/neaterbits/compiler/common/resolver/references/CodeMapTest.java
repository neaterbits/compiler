package com.neaterbits.compiler.common.resolver.references;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceName;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.BaseResolveTest;
import com.neaterbits.compiler.common.resolver.ReferenceType;
import com.neaterbits.compiler.common.resolver.TestDependency;
import com.neaterbits.compiler.common.resolver.TestResolvedFile;
import com.neaterbits.compiler.common.resolver.TestResolvedType;
import com.neaterbits.compiler.common.resolver.codemap.CodeMapImpl;
import com.neaterbits.compiler.common.resolver.codemap.ResolvedTypeCodeMapImpl;

public class CodeMapTest extends BaseResolveTest {

	@Test
	public void testClassCodeMap() {

		final ResolvedTypeCodeMapImpl codeMap = new ResolvedTypeCodeMapImpl(new CodeMapImpl());
		
		final TestResolvedFile testFile = new TestResolvedFile("TestFile.java");

		final List<String> namespace = Arrays.asList("com", "test");
		final String className = "TestClass";
		final ScopedName scopedName = new ScopedName(namespace, className);
		
		final ResolvedType resolvedClass = new TestResolvedType(testFile.getSpec(), scopedName, TypeVariant.CLASS, makeClassType(namespace, className));

		final int typeNo = codeMap.addType(resolvedClass);

		final int fileNo = codeMap.addFile(testFile, new int [] { typeNo });
		assertThat(fileNo).isGreaterThanOrEqualTo(0);

		final Collection<ResolvedType> directSubtypes = codeMap.getDirectSubtypes(resolvedClass.getFullTypeName());
		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.isEmpty());
		
		assertThat(codeMap.getClassExtendsFrom(resolvedClass.getFullTypeName())).isNull();

		final Collection<ResolvedType> allSubtypes = codeMap.getAllSubtypes(resolvedClass.getFullTypeName());
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes).isEmpty();
		
	}

	@Test
	public void testInterfaceCodeMap() {

		final ResolvedTypeCodeMapImpl codeMap = new ResolvedTypeCodeMapImpl(new CodeMapImpl());
		
		final TestResolvedFile testFile = new TestResolvedFile("TestFile.java");

		final List<String> namespace = Arrays.asList("com", "test");
		final String interfaceName = "TestInterface";
		final ScopedName scopedName = new ScopedName(namespace, interfaceName);
		
		final ResolvedType resolvedInterface = new TestResolvedType(testFile.getSpec(), scopedName, TypeVariant.INTERFACE, makeInterfaceType(namespace, interfaceName));

		final int typeNo = codeMap.addType(resolvedInterface);

		final int fileNo = codeMap.addFile(testFile, new int [] { typeNo });
		assertThat(fileNo).isGreaterThanOrEqualTo(0);

		final Collection<ResolvedType> directSubtypes = codeMap.getDirectSubtypes(resolvedInterface.getFullTypeName());
		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.isEmpty());
		
		assertThat(codeMap.getClassExtendsFrom(resolvedInterface.getFullTypeName())).isNull();

		final Collection<ResolvedType> allSubtypes = codeMap.getAllSubtypes(resolvedInterface.getFullTypeName());
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes).isEmpty();
	}

	@Test
	public void testClassImplementingInterface() {
		final ResolvedTypeCodeMapImpl codeMap = new ResolvedTypeCodeMapImpl(new CodeMapImpl());
		
		final TestResolvedFile classTestFile = new TestResolvedFile("TestClass.java");

		final TestResolvedFile interfaceTestFile = new TestResolvedFile("TestInterface.java");

		final List<String> classNamespace = Arrays.asList("com", "test");
		final String className = "TestClass";
		final ScopedName classScopedName = new ScopedName(classNamespace, className);
		
		final List<String> interfaceNamespace = Arrays.asList("com", "test");
		final String interfaceName = "TestInterface";
		
		final ScopedName interfaceScopedName = new ScopedName(interfaceNamespace, interfaceName);
		
		final ResolvedType resolvedInterface = new TestResolvedType(
				interfaceTestFile.getSpec(),
				interfaceScopedName,
				TypeVariant.INTERFACE,
				makeInterfaceType(interfaceNamespace, interfaceName));
		
		final ResolvedTypeDependency interfaceDependency = new TestDependency(resolvedInterface, ReferenceType.EXTENDS_FROM, null);
		final ResolvedType resolvedClass = new TestResolvedType(
				classTestFile.getSpec(),
				classScopedName,
				TypeVariant.CLASS,
				makeClassType(classNamespace, className),
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

		final List<ResolvedType> directSubtypes = codeMap.getDirectSubtypes(resolvedInterface.getFullTypeName());
		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.size()).isEqualTo(1);
		assertThat(directSubtypes.iterator().next()).isSameAs(resolvedClass);

		final List<ResolvedType> allSubtypes = codeMap.getAllSubtypes(resolvedInterface.getFullTypeName());
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes.size()).isEqualTo(1);
		assertThat(allSubtypes.iterator().next()).isSameAs(resolvedClass);

		assertThat(codeMap.getClassExtendsFrom(resolvedInterface.getFullTypeName())).isNull();

		final ResolvedType anotherClass = addType(codeMap, "AnotherTestClass.java", "com.test.AnotherTestClass", TypeVariant.CLASS, resolvedClass);
		final ResolvedType anotherInterface = addType(codeMap, "AnotherTestInterface.java", "com.test.AnotherTestInterface", TypeVariant.INTERFACE, resolvedInterface);

		final List<ResolvedType> directSubtypesUpdated = codeMap.getDirectSubtypes(resolvedInterface.getFullTypeName());

		assertThat(directSubtypesUpdated).isNotNull();
		assertThat(directSubtypesUpdated.size()).isEqualTo(2);
		assertThat(directSubtypesUpdated.get(0)).isSameAs(resolvedClass);
		assertThat(directSubtypesUpdated.get(1)).isSameAs(anotherInterface);

		final List<ResolvedType> allSubtypesUpdated = codeMap.getAllSubtypes(resolvedInterface.getFullTypeName());

		assertThat(allSubtypesUpdated).isNotNull();
		
		System.out.println("All subtypes: " + allSubtypesUpdated);
		
		assertThat(allSubtypesUpdated.size()).isEqualTo(3);
		assertThat(allSubtypesUpdated.get(0)).isSameAs(resolvedClass);
		assertThat(allSubtypesUpdated.get(1)).isSameAs(anotherInterface);
		assertThat(allSubtypesUpdated.get(2)).isSameAs(anotherClass);
	}

	private ResolvedType addType(ResolvedTypeCodeMapImpl codeMap, String file, String name, TypeVariant typeVariant, ResolvedType ... extendsFrom) {
		
		final ResolvedFile resolvedFile = new TestResolvedFile(file);

		final ResolvedType resolvedType = makeResolvedType(resolvedFile, name, typeVariant, extendsFrom);

		final int typeNo = codeMap.addType(resolvedType);

		codeMap.addFile(resolvedFile, new int [] { typeNo });

		return resolvedType;
	}
	
	private static ClassType makeClassType(Collection<String> namespace, String name) {
		
		final Context context = new Context("file", 0, 0, 0, 0, "");
		
		final ClassDefinition classDefinition = new ClassDefinition(
				context,
				new ClassModifiers(context, Collections.emptyList()),
				new ClassName(name),
				null,
				null,
				Collections.emptyList());
		
		return new ClassType(
				new NamespaceReference(namespace.toArray(new String[namespace.size()])),
				null,
				classDefinition);
	}

	private static InterfaceType makeInterfaceType(Collection<String> namespace, String name) {
		
		final Context context = new Context("file", 0, 0, 0, 0, "");
		
		final InterfaceDefinition interfaceDefinition = new InterfaceDefinition(
				context,
				new InterfaceModifiers(context, Collections.emptyList()),
				new InterfaceName(name),
				null,
				Collections.emptyList());
		
		return new InterfaceType(
				new NamespaceReference(namespace.toArray(new String[namespace.size()])),
				null,
				interfaceDefinition);
	}
}
