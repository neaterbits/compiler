package com.neaterbits.compiler.common.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.convert.OOToProceduralConverterState;

public class OOToProceduralConverter {

	public CompilationUnit convertCompilationUnit(CompilationUnit compilationUnit, OOToProceduralConverterState converterState) {

		final List<CompilationCode> convertedCode = new ArrayList<>();
		
		for (CompilationCode code : compilationUnit.getCode()) {
			if (code instanceof Namespace) {
				final Namespace namespace = (Namespace)code;
				
				converterState.setCurrentNamespace(namespace);
			}
			else {
				convertOOCode(code, converterState, convertedCode);
			}
		}
		
		return new CompilationUnit(compilationUnit.getContext(), convertedCode);
	}
	
	
	
	private List<CompilationCode> convertOOCode(CompilationCode code, OOToProceduralConverterState converterState, List<CompilationCode> allCode) {
		
		if (code instanceof ClassDefinition) {
			final ClassToFunctionsConverter classToFunctionsConverter = new ClassToFunctionsConverter();
			
			final List<CompilationCode> converted = classToFunctionsConverter.convertClass((ClassDefinition)code, converterState);
			
			allCode.addAll(converted);
		}
		else {
			throw new UnsupportedOperationException("Unknown code type " + code.getClass());
		}
		
		return allCode;
	}
}
