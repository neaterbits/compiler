package dev.nimbler.compiler.main;

import dev.nimbler.compiler.ast.objects.type.complex.ClassType;
import dev.nimbler.compiler.ast.objects.type.complex.StructType;
import dev.nimbler.compiler.convert.ootofunction.OOToProceduralClassDeclaration;

public class JavaToCClassDeclaration extends OOToProceduralClassDeclaration {

	public JavaToCClassDeclaration(ClassType classType, StructType dataFieldStructType, StructType vtableStructType) {
		super(classType, dataFieldStructType, vtableStructType);
	}
}
