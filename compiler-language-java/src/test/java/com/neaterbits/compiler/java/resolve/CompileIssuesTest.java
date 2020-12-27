package com.neaterbits.compiler.java.resolve;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.objects.expression.PrimaryList;
import com.neaterbits.compiler.ast.objects.expression.literal.NamePrimary;
import com.neaterbits.compiler.ast.objects.statement.ReturnStatement;
import com.neaterbits.compiler.java.BaseCompilerTest;
import com.neaterbits.compiler.java.CompiledAndResolvedFile;
import com.neaterbits.compiler.types.method.MethodInvocationType;
import com.neaterbits.util.parse.ParserException;

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
		
		final NamePrimary namePrimary = get(iterator);
		assertThat(namePrimary.getName()).isEqualTo("integer");

		final MethodInvocationExpression expression = get(iterator);
		
		assertThat(expression.getInvocationType()).isEqualTo(MethodInvocationType.UNRESOLVED);
		assertThat(expression.getCallable().getName()).isEqualTo("toString");
		
		assertThat(compiled.getErrors().isEmpty()).isTrue();
	}
}
