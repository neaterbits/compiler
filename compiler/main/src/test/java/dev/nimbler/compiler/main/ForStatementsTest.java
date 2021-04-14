package dev.nimbler.compiler.main;

import java.io.IOException;

import org.junit.Test;

import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.language.java.emit.JavaCompilationUnitEmitter;

public class ForStatementsTest extends BaseJavaCompilerTest {

	final String FILENAME = "src/test/java/com/neaterbits/compiler/main/ForStatementTestClass.java";

	@Test
	public void testEmitCompilation() throws IOException, ParserException {
		final CompilationUnit compilationUnit = compile(FILENAME);
		
		final String emitted = emitCompilationUnit(compilationUnit, new JavaCompilationUnitEmitter());
		
		System.out.println(emitted);
	}
}
