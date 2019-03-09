package com.neaterbits.compiler.common.resolver.codemap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.common.loader.TypeVariant;

public class TypeHierarchyTest {

	@Test
	public void testAdd() {
		
		final TypeHierarchy typeHierarchy = new TypeHierarchy();
		
		for (int i = 0; i < 100000; ++ i) {
			final int typeNo = typeHierarchy.addType(TypeVariant.CLASS, null, null);

			assertThat(typeNo).isEqualTo(i);
		}
	}
}
