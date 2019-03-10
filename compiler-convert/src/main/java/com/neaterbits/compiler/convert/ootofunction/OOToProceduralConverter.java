package com.neaterbits.compiler.convert.ootofunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.CompilationCodeLines;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.Namespace;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.convert.OOToProceduralConverterState;

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
			//	convertOOCode(code, converterState, convertedCode);
			}
		}
		
		System.out.println("### converted code list " + convertedCode);

		return new CompilationUnit(compilationUnit.getContext(), Collections.emptyList(), convertedCode);
	}

	private void convertCode(CompilationCodeLines codeLines, T converterState, List<CompilationCode> allCode) {
		
		for (CompilationCode compilationCode : codeLines.getCode()) {

			if (compilationCode instanceof ClassDefinition) {
				
				final ClassToFunctionsConverter<T> classToFunctionsConverter = new ClassToFunctionsConverter<>();
				
				final ClassDefinition classDefinition = (ClassDefinition)compilationCode;
				
				final List<CompilationCode> converted = classToFunctionsConverter.convertClass(
						converterState.makeCompleteName(classDefinition.getName().getName()),
						classDefinition,
						converterState);
				
				allCode.addAll(converted);
			}
			else {
				throw new UnsupportedOperationException("Unknown code type " + compilationCode.getClass());
			}
		}
	}
}
