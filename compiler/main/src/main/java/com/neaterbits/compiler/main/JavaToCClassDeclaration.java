package com.neaterbits.compiler.main;

import com.neaterbits.compiler.ast.objects.type.complex.ClassType;
import com.neaterbits.compiler.ast.objects.type.complex.StructType;
import com.neaterbits.compiler.convert.ootofunction.OOToProceduralClassDeclaration;

public class JavaToCClassDeclaration extends OOToProceduralClassDeclaration {

	public JavaToCClassDeclaration(ClassType classType, StructType dataFieldStructType, StructType vtableStructType) {
		super(classType, dataFieldStructType, vtableStructType);
	}
}
