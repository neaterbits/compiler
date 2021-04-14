package com.neaterbits.compiler.convert.ootofunction;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.type.complex.ClassType;
import com.neaterbits.compiler.ast.objects.type.complex.StructType;
import com.neaterbits.language.common.types.TypeName;

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

	public TypeName getTypeName() {
		return classType.getTypeName();
	}
	
	public StructType getDataFieldStructType() {
		return dataFieldStructType;
	}

	public StructType getVTableStructType() {
		return vtableStructType;
	}
}
