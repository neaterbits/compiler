package com.neaterbits.compiler.common.resolver.references;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.BaseResolveTest;
import com.neaterbits.compiler.common.resolver.ReferenceType;
import com.neaterbits.compiler.common.resolver.TestDependency;
import com.neaterbits.compiler.common.resolver.TestResolvedFile;
import com.neaterbits.compiler.common.resolver.TestResolvedType;

public class ReferencesTest extends BaseResolveTest {

	@Test
	public void testClassReferences() {

		final References references = new References();
		
		final TestResolvedFile testFile = new TestResolvedFile("TestFile.java");
		final int fileNo = references.addFile(testFile);
		assertThat(fileNo).isGreaterThanOrEqualTo(0);

		final ScopedName scopedName = new ScopedName(Arrays.asList("com", "test"), "TestClass");
		
		final ResolvedType resolvedClass = new TestResolvedType(testFile.getSpec(), scopedName, TypeVariant.CLASS, null);

		references.addType(fileNo, resolvedClass);

		final Collection<ResolvedType> directSubtypes = references.getDirectSubtypes(resolvedClass.getSpec());
		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.isEmpty());
		
		assertThat(references.getClassExtendsFrom(resolvedClass.getSpec())).isNull();

		final Collection<ResolvedType> allSubtypes = references.getAllSubtypes(resolvedClass.getSpec());
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes).isEmpty();
		
	}

	@Test
	public void testInterfaceReferences() {

		final References references = new References();
		
		final TestResolvedFile testFile = new TestResolvedFile("TestFile.java");
		final int fileNo = references.addFile(testFile);
		assertThat(fileNo).isGreaterThanOrEqualTo(0);

		final ScopedName scopedName = new ScopedName(Arrays.asList("com", "test"), "TestInterface");
		
		final ResolvedType resolvedInterface = new TestResolvedType(testFile.getSpec(), scopedName, TypeVariant.INTERFACE, null);

		references.addType(fileNo, resolvedInterface);

		final Collection<ResolvedType> directSubtypes = references.getDirectSubtypes(resolvedInterface.getSpec());
		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.isEmpty());
		
		assertThat(references.getClassExtendsFrom(resolvedInterface.getSpec())).isNull();

		final Collection<ResolvedType> allSubtypes = references.getAllSubtypes(resolvedInterface.getSpec());
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes).isEmpty();
	}

	@Test
	public void testClassImplementingInterface() {
		final References references = new References();
		
		final TestResolvedFile classTestFile = new TestResolvedFile("TestClass.java");
		final int classFileNo = references.addFile(classTestFile);
		assertThat(classFileNo).isGreaterThanOrEqualTo(0);

		final TestResolvedFile interfaceTestFile = new TestResolvedFile("TestInterface.java");
		final int interfaceFileNo = references.addFile(interfaceTestFile);
		assertThat(interfaceFileNo).isEqualTo(classFileNo + 1);

		final ScopedName classScopedName = new ScopedName(Arrays.asList("com", "test"), "TestClass");
		final ScopedName interfaceScopedName = new ScopedName(Arrays.asList("com", "test"), "TestInterface");
		
		final ResolvedType resolvedInterface = new TestResolvedType(interfaceTestFile.getSpec(), interfaceScopedName, TypeVariant.INTERFACE, null);
		
		final ResolvedTypeDependency interfaceDependency = new TestDependency(resolvedInterface, ReferenceType.EXTENDS_FROM, null);
		final ResolvedType resolvedClass = 	   new TestResolvedType(classTestFile.getSpec(), classScopedName, TypeVariant.CLASS, null, null, Arrays.asList(interfaceDependency), null);

		final int interfaceTypeNo = references.addType(interfaceFileNo, resolvedInterface);
		final int classTypeNo = references.addType(classFileNo, resolvedClass);
		
		assertThat(classTypeNo).isEqualTo(interfaceTypeNo + 1);

		final List<ResolvedType> directSubtypes = references.getDirectSubtypes(resolvedInterface.getSpec());
		assertThat(directSubtypes).isNotNull();
		assertThat(directSubtypes.size()).isEqualTo(1);
		assertThat(directSubtypes.iterator().next()).isSameAs(resolvedClass);

		final List<ResolvedType> allSubtypes = references.getAllSubtypes(resolvedInterface.getSpec());
		assertThat(allSubtypes).isNotNull();
		assertThat(allSubtypes.size()).isEqualTo(1);
		assertThat(allSubtypes.iterator().next()).isSameAs(resolvedClass);

		assertThat(references.getClassExtendsFrom(resolvedInterface.getSpec())).isNull();

		final ResolvedType anotherClass = addType(references, "AnotherTestClass.java", "com.test.AnotherTestClass", TypeVariant.CLASS, resolvedClass);
		final ResolvedType anotherInterface = addType(references, "AnotherTestInterface.java", "com.test.AnotherTestInterface", TypeVariant.INTERFACE, resolvedInterface);

		final List<ResolvedType> directSubtypesUpdated = references.getDirectSubtypes(resolvedInterface.getSpec());

		assertThat(directSubtypesUpdated).isNotNull();
		assertThat(directSubtypesUpdated.size()).isEqualTo(2);
		assertThat(directSubtypesUpdated.get(0)).isSameAs(resolvedClass);
		assertThat(directSubtypesUpdated.get(1)).isSameAs(anotherInterface);

		final List<ResolvedType> allSubtypesUpdated = references.getAllSubtypes(resolvedInterface.getSpec());

		assertThat(allSubtypesUpdated).isNotNull();
		assertThat(allSubtypesUpdated.size()).isEqualTo(3);
		assertThat(allSubtypesUpdated.get(0)).isSameAs(resolvedClass);
		assertThat(allSubtypesUpdated.get(1)).isSameAs(anotherInterface);
		assertThat(allSubtypesUpdated.get(2)).isSameAs(anotherClass);
	}

	private ResolvedType addType(References references, String file, String name, TypeVariant typeVariant, ResolvedType ... extendsFrom) {
		
		final ResolvedFile resolvedFile = new TestResolvedFile(file);
		final int fileNo = references.addFile(resolvedFile);

		final ResolvedType resolvedType = makeResolvedType(resolvedFile, name, typeVariant, extendsFrom);

		references.addType(fileNo, resolvedType);
		
		return resolvedType;
	}
}
