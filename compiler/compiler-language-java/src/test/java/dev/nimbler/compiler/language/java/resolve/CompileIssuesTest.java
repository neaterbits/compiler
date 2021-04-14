package dev.nimbler.compiler.language.java.resolve;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.expression.PrimaryList;
import dev.nimbler.compiler.ast.objects.expression.UnresolvedMethodInvocationExpression;
import dev.nimbler.compiler.ast.objects.expression.literal.UnresolvedNamePrimary;
import dev.nimbler.compiler.ast.objects.statement.ReturnStatement;
import dev.nimbler.compiler.language.java.compile.BaseCompilerTest;
import dev.nimbler.compiler.language.java.compile.CompiledAndResolvedFile;

// Test various compile and resolve issues
public class CompileIssuesTest extends BaseCompilerTest {

	@Test
	public void testUnresolvedTypeWhenCallingMethod() throws IOException, ParserException {

		final String source =
				
				"package com.test;\n"
			+   "class ClassToTest {\n"
			+   " private Integer integer;\n"
			
			+   " public String toString() {;\n"
			+   "  return integer.toString();\n"
			+   " }\n"

			+   "}\n";

		final CompiledAndResolvedFile compiled = compile(
				"ClassForTest.java",
				source,
				new TestResolvedTypes()
					.addType("java.lang.Integer")
					.addType("java.lang.String"));
		
		final Iterator<BaseASTElement> iterator = compiled.getASTElements(BaseASTElement.class).iterator();
		
		final ReturnStatement returnStatement = getNext(iterator, ReturnStatement.class);
		
		assertThat(returnStatement).isNotNull();
		
		final PrimaryList primaryList = (PrimaryList)get(iterator);
		assertThat(primaryList).isNotNull();
		
		final UnresolvedNamePrimary namePrimary = get(iterator);
		assertThat(namePrimary.getName()).isEqualTo("integer");

		final UnresolvedMethodInvocationExpression expression = get(iterator);
		assertThat(expression.getCallable().getName()).isEqualTo("toString");
		
		assertThat(compiled.getErrors().isEmpty()).isTrue();
	}
}
