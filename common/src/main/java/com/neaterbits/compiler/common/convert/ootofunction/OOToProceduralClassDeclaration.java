package com.neaterbits.compiler.common.convert.ootofunction;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.StructType;

public class OOToProceduralClassDeclaration {

	private final ClassType classType;
	private final StructType dataFieldStructType;
	private final StructType vtableStructType;

	protected OOToProceduralClassDeclaration(ClassType classType, StructType dataFieldStructType, StructType vtableStructType) {
		
		Objects.requireNonNull(classType);
		Objects.requireNonNull(dataFieldStructType);
		Objects.requireNonNull(vtableStructType);

		this.classType = classType;
		this.dataFieldStructType = dataFieldStructType;
		this.vtableStructType = vtableStructType;
	}

	public ClassType getClassType() {
		return classType;
	}

	public StructType getDataFieldStructType() {
		return dataFieldStructType;
	}

	public StructType getVTableStructType() {
		return vtableStructType;
	}
}
