package com.neaterbits.compiler.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.neaterbits.compiler.c.emit.CProgramEmmiter;
import com.neaterbits.compiler.common.antlr4.AntlrError;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.block.FunctionName;
import com.neaterbits.compiler.common.ast.block.MethodName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.StructName;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterState;
import com.neaterbits.compiler.common.convert.ootofunction.OOToProceduralConverter;
import com.neaterbits.compiler.common.emit.EmitterState;
import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.compiler.java.parser.antlr4.Java8AntlrParser;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaToCConverterTest {

	@Test
	public void testJavaToC() throws IOException {
		
		final Java8AntlrParser parser = new Java8AntlrParser(true);

		final List<AntlrError> errors = new ArrayList<>();
		
		final CompilationUnit compilationUnit;
		
		try (FileInputStream inputStream = new FileInputStream("src/test/java/com/neaterbits/compiler/main/JavaToCConverterTest.java")) {
			compilationUnit = parser.parse(inputStream, errors);
		}
		
		assertThat(errors.isEmpty()).isTrue();
		assertThat(compilationUnit).isNotNull();
		
		final OOToProceduralConverter converter = new OOToProceduralConverter();
		
		final OOToProceduralConverterState converterState = new OOToProceduralConverterState() {
			@Override
			public FunctionName methodToFunctionName(Namespace namespace, MethodName methodName) {
				return new FunctionName(Strings.join(namespace.getParts(), '_') + '_' + methodName.getName());
			}
			
			@Override
			public StructName classToStructName(Namespace namespace, ClassName className) {
				return new StructName(Strings.join(namespace.getParts(), '_') + '_' + className.getName());
			}
		};

		final CompilationUnit cCode = converter.convertCompilationUnit(compilationUnit, converterState);
		
		final CProgramEmmiter cEmitter = new CProgramEmmiter();
		
		final EmitterState emitterState = new EmitterState('\n');
		
		for (CompilationCode code : cCode.getCode()) {
			code.visit(cEmitter, emitterState);
		}
		
		System.out.println("Emitted code:\n" + emitterState.asString());
		
	}
}
