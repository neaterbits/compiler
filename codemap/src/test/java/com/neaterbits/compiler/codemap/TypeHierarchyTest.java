package com.neaterbits.compiler.codemap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.codemap.TypeHierarchy;
import com.neaterbits.compiler.codemap.TypeVariant;

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
