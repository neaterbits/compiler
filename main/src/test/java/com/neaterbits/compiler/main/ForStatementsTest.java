package com.neaterbits.compiler.main;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.java.emit.JavaCompilationUnitEmitter;

public class ForStatementsTest extends BaseJavaCompilerTest {

	final String FILENAME = "src/test/java/com/neaterbits/compiler/main/ForStatementTestClass.java";

	@Test
	public void testEmitCompilation() throws IOException {
		final CompilationUnit compilationUnit = compile(FILENAME);
		
		final String emitted = emitCompilationUnit(compilationUnit, new JavaCompilationUnitEmitter());
		
		System.out.println(emitted);
	}
}
