package com.neaterbits.compiler.main;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.common.antlr4.AntlrError;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.emit.ProgramEmitter;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;

public abstract class BaseJavaCompilerTest {
	
	final CompilationUnit compile(String fileName) throws IOException {
		final Java8AntlrParser parser = new Java8AntlrParser(true);
		final List<AntlrError> errors = new ArrayList<>();

		final CompilationUnit compilationUnit;

		try (FileInputStream inputStream = new FileInputStream(fileName)) {
			compilationUnit = parser.parse(inputStream, errors, new ParseLogger(System.out));
		}

		assertThat(errors.isEmpty()).isTrue();
		assertThat(compilationUnit).isNotNull();
		
		return compilationUnit;
	}

	final ClassDefinition compileAndReturnClass(String fileName) throws IOException {
		
		final CompilationUnit compilationUnit = compile(fileName);
		
		assertThat(compilationUnit.getCode().size()).isEqualTo(1);
		
		final CompilationCode code = compilationUnit.getCode().iterator().next();
		
		assertThat(code instanceof Namespace).isTrue();
		
		final Namespace nameSpace = (Namespace)code;
		
		assertThat(nameSpace.getLines()).isNotNull();
		assertThat(nameSpace.getLines().getCode().size()).isEqualTo(1);
		
		final CompilationCode namespaceCode = nameSpace.getLines().getCode().iterator().next();
		
		assertThat(namespaceCode instanceof ClassDefinition).isTrue();

		return (ClassDefinition)namespaceCode;
	}

	
	static String emitCompilationUnit(CompilationUnit compilationUnit, ProgramEmitter<EmitterState> emitter) {
		final EmitterState emitterState = new EmitterState('\n');

		for (CompilationCode code : compilationUnit.getCode()) {
			code.visit(emitter, emitterState);
		}

		return emitterState.asString();		
	}
}
