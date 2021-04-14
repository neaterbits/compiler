package dev.nimbler.compiler.convert.ootofunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.nimbler.compiler.ast.objects.CompilationCode;
import dev.nimbler.compiler.ast.objects.CompilationCodeLines;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.ast.objects.Namespace;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.convert.OOToProceduralConverterState;

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

		return new CompilationUnit(compilationUnit.getContext(), Collections.emptyList(), convertedCode, null);
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
