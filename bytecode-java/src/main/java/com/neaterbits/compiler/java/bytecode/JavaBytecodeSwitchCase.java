package com.neaterbits.compiler.java.bytecode;

import static com.neaterbits.compiler.java.bytecode.JavaBytecodes.*;

import java.util.Objects;

import com.neaterbits.compiler.bytecode.common.BytecodeInstructions;
import com.neaterbits.compiler.bytecode.common.BytecodeSwitchCase;
import com.neaterbits.compiler.bytecode.common.TypeMap;
import com.neaterbits.compiler.common.TypeName;

public final class JavaBytecodeSwitchCase extends BytecodeSwitchCase {

	private final ClassFile classBytecode;
	private final TypeMap typeMap;
	
	public JavaBytecodeSwitchCase(ClassFile classBytecode, TypeMap typeMap) {
		
		Objects.requireNonNull(classBytecode);
		Objects.requireNonNull(typeMap);

		this.classBytecode = classBytecode;
		this.typeMap = typeMap;
	}
	
	private int getType(byte [] bytecodes, int index) {
		
		final int classIndex = bytecodes[index + 1] << 8 | bytecodes[index + 2];
		
		final TypeName typeName = classBytecode.getReferencedTypeName(classIndex);
		
		final int type = typeMap.getTypeNo(typeName);
		
		if (type < 0) {
			throw new IllegalStateException();
		}

		return type;
	}


	@Override
	public int switchCase(byte [] bytecodes, int index, BytecodeInstructions listener) {
		
		final int length;
		
		switch (Byte.toUnsignedInt(bytecodes[index + 0])) {
		case AALOAD:
			listener.loadReferenceFromArray(index);
			length = 1;
			break;
			
		case AASTORE:
			listener.storeReferenceInArray(index);
			length = 1;
			break;
			
		case ACONST_NULL:
			listener.pushNullReference(index);
			length = 1;
			break;
			
		case ALOAD:
			listener.pushReferenceFromVariable(index, bytecodes[index + 1]);
			length = 2;
			break;
			
		case ALOAD_0:
			listener.pushReferenceFromVariable(index, 0);
			length = 1;
			break;
			
		case ALOAD_1:
			listener.pushReferenceFromVariable(index, 1);
			length = 1;
			break;
		
		case ALOAD_2:
			listener.pushReferenceFromVariable(index, 2);
			length = 1;
			break;

		case ALOAD_3:
			listener.pushReferenceFromVariable(index, 3);
			length = 1;
			break;

		case ANEWARRAY:
			listener.createArray(index, getType(bytecodes, index));
			length = 3;
			break;
			
		default:
			throw new UnsupportedOperationException();
		}
		
		return length;
	}
}
