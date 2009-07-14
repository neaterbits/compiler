package com.neaterbits.compiler.common.convert.ootofunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationCodeLines;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterState;

public abstract class OOToProceduralConverter<T extends OOToProceduralConverterState<T>> {

	public final CompilationUnit convertCompilationUnit(CompilationUnit compilationUnit, T converterState) {

		final List<CompilationCode> convertedCode = new ArrayList<>();

		System.out.println("### convert code " + compilationUnit.getCode());

		for (CompilationCode code : compilationUnit.getCode()) {
			if (code instanceof Namespace) {
				final Namespace namespace = (Namespace)code;
				
				converterState.setCurrentNamespace(namespace);

				convertCode(namespace.getLines(), converterState, convertedCode);
			}
			else {
				convertOOCode(code, converterState, convertedCode);
			}
		}

		return new CompilationUnit(compilationUnit.getContext(), Collections.emptyList(), convertedCode);
	}

	private void convertCode(CompilationCodeLines codeLines, T converterState, List<CompilationCode> allCode) {
		
		for (CompilationCode compilationCode : codeLines.getCode()) {
			final List<CompilationCode> convertedCode = convertOOCode(compilationCode, converterState, allCode);
			
			allCode.addAll(convertedCode);
		}
	}
	
	
	private List<CompilationCode> convertOOCode(CompilationCode code, T converterState, List<CompilationCode> allCode) {

		System.out.println("### convert OO code " + code);

		if (code instanceof ClassDefinition) {
			final ClassToFunctionsConverter<T> classToFunctionsConverter = new ClassToFunctionsConverter<>();
			
			final List<CompilationCode> converted = classToFunctionsConverter.convertClass((ClassDefinition)code, converterState);
			
			allCode.addAll(converted);
		}
		else {
			throw new UnsupportedOperationException("Unknown code type " + code.getClass());
		}
		
		System.out.println("## return allCode " + allCode);
		
		return allCode;
	}
}
