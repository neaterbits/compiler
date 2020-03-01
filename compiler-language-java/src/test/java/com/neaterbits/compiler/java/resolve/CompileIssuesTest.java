package com.neaterbits.compiler.java.resolve;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.expression.MethodInvocationExpression;
import com.neaterbits.compiler.ast.objects.statement.ReturnStatement;
import com.neaterbits.compiler.ast.objects.variables.NameReference;
import com.neaterbits.compiler.java.BaseCompilerTest;
import com.neaterbits.compiler.util.method.MethodInvocationType;
import com.neaterbits.compiler.util.model.CompiledAndResolvedFile;

// Test various compile and resolve issues
public class CompileIssuesTest extends BaseCompilerTest {

	@Test
	public void testUnresolvedTypeWhenCallingMethod() throws IOException {

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
					.addType("java.lang.Integer"));
		
		final Iterator<BaseASTElement> iterator = compiled.getASTElements(BaseASTElement.class).iterator();
		
		final ReturnStatement returnStatement = getNext(iterator, ReturnStatement.class);
		
		assertThat(returnStatement).isNotNull();
		
		System.out.println("## expression " + returnStatement.getExpression());
		
		final MethodInvocationExpression expression = get(iterator);
		
		assertThat(expression.getInvocationType()).isEqualTo(MethodInvocationType.VARIABLE_REFERENCE);
		assertThat(expression.getCallable().getName()).isEqualTo("toString");
		
		final NameReference nameReference = get(iterator);
		assertThat(nameReference).isNotNull();
		
		assertThat(compiled.getErrors().isEmpty()).isTrue();
		
	}
}
