package com.neaterbits.compiler.main;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.statement.IfElseIfElseStatement;
import com.neaterbits.compiler.ast.objects.statement.Statement;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.java.emit.JavaCompilationUnitEmitter;
import com.neaterbits.util.parse.ParserException;

public class IfStatementsTest extends BaseJavaCompilerTest {

	final String FILENAME = "src/test/java/com/neaterbits/compiler/main/IfStatementTestClass.java";

	@Test
	public void testEmitCompilation() throws IOException, ParserException {
		final CompilationUnit compilationUnit = compile(FILENAME);
		
		final String emitted = emitCompilationUnit(compilationUnit, new JavaCompilationUnitEmitter());
		
		System.out.println(emitted);
	}
	
	@Test
	public void testIfStatementsMethod1() throws IOException, ParserException {
		
		final Block block = checkGetMethodBlock("testMethod1");

		assertThat(block.getStatements().size()).isEqualTo(1);
		
		final Statement statement = block.getStatements().iterator().next();
		
		assertThat(statement instanceof IfElseIfElseStatement).isTrue();
	
		final IfElseIfElseStatement ifElseIfElse = (IfElseIfElseStatement)statement;
		
		assertThat(ifElseIfElse.getConditions().size()).isEqualTo(3);
		
		assertThat(ifElseIfElse.getElseBlock()).isNotNull();
	}
	
	@Test
	public void testIfStatementsMethod2() throws IOException, ParserException {

		final Block block = checkGetMethodBlock("testMethod2");

		assertThat(block.getStatements().size()).isEqualTo(2);
		
		final Statement statement = block.getStatements().iterator().next();
		
		assertThat(statement instanceof IfElseIfElseStatement).isTrue();
	
		final IfElseIfElseStatement ifElseIfElse = (IfElseIfElseStatement)statement;
		
		assertThat(ifElseIfElse.getConditions().size()).isEqualTo(3);
		
		assertThat(ifElseIfElse.getElseBlock()).isNotNull();
	}

	@Test
	public void testIfStatementsMethod3() throws IOException, ParserException {

		final Block block = checkGetMethodBlock("testMethod3");

		assertThat(block).isNotNull();
		
		assertThat(block.getStatements().size()).isEqualTo(1);
		
		final Statement statement = block.getStatements().iterator().next();
		
		assertThat(statement instanceof IfElseIfElseStatement).isTrue();
	
		final IfElseIfElseStatement ifElseIfElse = (IfElseIfElseStatement)statement;
		
		assertThat(ifElseIfElse.getConditions().size()).isEqualTo(3);
		
		assertThat(ifElseIfElse.getElseBlock()).isNull();
	}

	@Test
	public void testIfStatementsMethod4() throws IOException, ParserException {

		final Block block = checkGetMethodBlock("testMethod4");

		assertThat(block).isNotNull();
		
		assertThat(block.getStatements().size()).isEqualTo(2);
		
		final Statement statement = block.getStatements().iterator().next();
		
		assertThat(statement instanceof IfElseIfElseStatement).isTrue();
	
		final IfElseIfElseStatement ifElseIfElse = (IfElseIfElseStatement)statement;
		
		assertThat(ifElseIfElse.getConditions().size()).isEqualTo(3);
		
		assertThat(ifElseIfElse.getElseBlock()).isNull();
	}

	@Test
	public void testIfStatementsMethod5() throws IOException, ParserException {

		final Block block = checkGetMethodBlock("testMethod5");

		assertThat(block).isNotNull();
		
		assertThat(block.getStatements().size()).isEqualTo(1);
		
		final Statement statement = block.getStatements().iterator().next();
		
		assertThat(statement instanceof IfElseIfElseStatement).isTrue();
	
		final IfElseIfElseStatement ifElseIfElse = (IfElseIfElseStatement)statement;
		
		assertThat(ifElseIfElse.getConditions().size()).isEqualTo(3);
		
		assertThat(ifElseIfElse.getElseBlock()).isNull();
	}

	private Block checkGetMethodBlock(String methodName) throws IOException, ParserException {

		final ClassDefinition classDefinition = compileAndReturnClass(FILENAME);
		
		final ClassMethodMember testMethod1 = classDefinition.findMethod(methodName);
		
		assertThat(testMethod1).isNotNull();
		
		final Block block = testMethod1.getMethod().getBlock();
		assertThat(block).isNotNull();

		return block;
	}
}
