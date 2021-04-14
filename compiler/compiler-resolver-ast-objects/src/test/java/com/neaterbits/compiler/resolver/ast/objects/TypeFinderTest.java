package com.neaterbits.compiler.resolver.ast.objects;

import org.junit.Test;

import com.neaterbits.compiler.util.TypeResolveMode;
import com.neaterbits.util.Strings;

import static org.assertj.core.api.Assertions.assertThat;

public class TypeFinderTest {

	@Test
	public void testFindExpressionPart() {

		assertThat(findExpressionPart(TypeResolveMode.COMPLETE_TO_COMPLETE, "com.test.Class.field", "com.test.Class")).isEqualTo("field");
		assertThat(findExpressionPart(TypeResolveMode.COMPLETE_TO_COMPLETE, "com.test.Class.NestedClass.field", "com.test.Class.NestedClass")).isEqualTo("field");
		assertThat(findExpressionPart(TypeResolveMode.COMPLETE_TO_COMPLETE, "com.test.Class.field.nestedfield", "com.test.Class")).isEqualTo("field.nestedfield");
		assertThat(findExpressionPart(TypeResolveMode.COMPLETE_TO_COMPLETE, "com.test.Class.NestedClass.field.nestedfield", "com.test.Class.NestedClass")).isEqualTo("field.nestedfield");
		assertThat(findExpressionPart(TypeResolveMode.COMPLETE_TO_COMPLETE, "com.test.Class", "com.test.Class")).isNull();

		assertThat(findExpressionPart(TypeResolveMode.CLASSNAME_TO_COMPLETE, "Class.field", "com.test.Class")).isEqualTo("field");
		assertThat(findExpressionPart(TypeResolveMode.CLASSNAME_TO_COMPLETE, "Class.NestedClass.field", "com.test.Class.NestedClass")).isEqualTo("field");
		assertThat(findExpressionPart(TypeResolveMode.CLASSNAME_TO_COMPLETE, "Class.field.nestedfield", "com.test.Class")).isEqualTo("field.nestedfield");
		assertThat(findExpressionPart(TypeResolveMode.CLASSNAME_TO_COMPLETE, "Class.NestedClass.field.nestedfield", "com.test.Class.NestedClass")).isEqualTo("field.nestedfield");
		assertThat(findExpressionPart(TypeResolveMode.CLASSNAME_TO_COMPLETE, "Class", "com.test.Class")).isNull();
	}

	private String findExpressionPart(TypeResolveMode typeResolveMode, String toResolveParts, String typeScopedNameParts) {
		
		final String [] expressionPart = MethodInvocationExpressionResolver.findExpressionPart(
				typeResolveMode,
				Strings.split(toResolveParts, '.'),
				Strings.split(typeScopedNameParts, '.'));
	
		return expressionPart != null ? Strings.join(expressionPart, '.') : null;
	}
}
