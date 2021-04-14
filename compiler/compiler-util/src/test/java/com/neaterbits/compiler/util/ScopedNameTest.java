package com.neaterbits.compiler.util;

import java.util.Arrays;

import org.junit.Test;

import com.neaterbits.build.types.ScopedName;

import static org.assertj.core.api.Assertions.assertThat;

public class ScopedNameTest {

	@Test
	public void testStartsWith() {
		
		final ScopedName scope = new ScopedName(
				Arrays.asList("com", "test", "scope"),
				"TestClass");


		assertThat(scope.scopeStartsWith(new String [] { "com", "test" })).isTrue();
		assertThat(scope.scopeStartsWith(new String [] { "com", "tst" })).isFalse();
		assertThat(scope.scopeStartsWith(new String [] { "com", "test", "nonematch" })).isFalse();

	}
	
	@Test
	public void testRemove() {
		
		final ScopedName scope = new ScopedName(
				Arrays.asList("com", "test", "scope", "unittest"),
				"TestClass");


		assertThat(scope.removeFromScope(new String [] { "com", "test" }).getScope())
					.isEqualTo(Arrays.asList("scope", "unittest"));

	}
	
}
