package dev.nimbler.language.bytecode.java.reader;

import java.io.DataInput;
import java.io.IOException;
import java.util.Objects;

public class DebugReaderListener implements ClassFileReaderListener {

	private final ClassFileReaderListener delegate;
	
	public DebugReaderListener() {

		this.delegate = null;
	}
	
	public DebugReaderListener(ClassFileReaderListener delegate) {

		Objects.requireNonNull(delegate);
		
		this.delegate = delegate;
	}


	@Override
	public void onConstantPoolCount(int count) {
		System.out.println("ConstantPoolCount " + count);

		if (delegate != null) {
			delegate.onConstantPoolCount(count);
		}
	}

	@Override
	public void onConstantPoolClass(int index, int nameIndex) {
		System.out.println("ConstantPool " + index + " Class");
		
		if (delegate != null) {
			delegate.onConstantPoolClass(index, nameIndex);
		}
	}

	@Override
	public void onConstantPoolFieldref(int index, int classIndex, int nameAndTypeIndex) {
		System.out.println("ConstantPool " + index + " Fieldref");
		
		if (delegate != null) {
			delegate.onConstantPoolFieldref(index, classIndex, nameAndTypeIndex);
		}
	}

	@Override
	public void onConstantPoolMethodref(int index, int classIndex, int nameAndTypeIndex) {
		System.out.println("ConstantPool " + index + " Methodref " + classIndex + " " + nameAndTypeIndex);
		
		if (delegate != null) {
			delegate.onConstantPoolMethodref(index, classIndex, nameAndTypeIndex);
		}
	}

	@Override
	public void onConstantPoolInterfaceMethodref(int index, int classIndex, int nameAndTypeIndex) {
		System.out.println("ConstantPool " + index + " InterfaceMethodref");
		
		if (delegate != null) {
			delegate.onConstantPoolInterfaceMethodref(index, classIndex, nameAndTypeIndex);
		}
	}

	@Override
	public void onConstantPoolString(int index, int stringIndex) {
		System.out.println("ConstantPool " + index + " String");
		
		if (delegate != null) {
			delegate.onConstantPoolString(index, stringIndex);
		}
	}

	@Override
	public void onConstantPoolInteger(int index, int value) {
		System.out.println("ConstantPool " + index + " Integer");
		
		if (delegate != null) {
			delegate.onConstantPoolInteger(index, value);
		}
	}

	@Override
	public void onConstantPoolFloat(int index, float value) {
		System.out.println("ConstantPool " + index + " Float");
		
		if (delegate != null) {
			delegate.onConstantPoolFloat(index, value);
		}
	}

	@Override
	public void onConstantPoolLong(int index, long value) {
		System.out.println("ConstantPool " + index + " Long");
		
		if (delegate != null) {
			delegate.onConstantPoolLong(index, value);
		}
	}

	@Override
	public void onConstantPoolDouble(int index, double value) {
		System.out.println("ConstantPool " + index + " Double");
		
		if (delegate != null) {
			delegate.onConstantPoolDouble(index, value);
		}
	}

	@Override
	public void onConstantPoolNameAndType(int index, int nameIndex, int descriptorIndex) {
		System.out.println("ConstantPool " + index + " NameAndType");
		
		if (delegate != null) {
			delegate.onConstantPoolNameAndType(index, nameIndex, descriptorIndex);
		}
	}

	@Override
	public void onConstantPoolUTF8(int index, String value) {
		System.out.println("ConstantPool " + index + " UTF8 " + value);
		
		if (delegate != null) {
			delegate.onConstantPoolUTF8(index, value);
		}
	}

	@Override
	public void onConstantPoolMethodHandle(int index, int referenceKind, int referenceIndex) {
		System.out.println("ConstantPool " + index + " MethodHandlke");
		
		if (delegate != null) {
			delegate.onConstantPoolMethodHandle(index, referenceKind, referenceIndex);
		}
	}

	@Override
	public void onConstantPoolMethodType(int index, int descriptorIndex) {
		System.out.println("ConstantPool " + index + " MethodType");
		
		if (delegate != null) {
			delegate.onConstantPoolMethodType(index, descriptorIndex);
		}
	}

	@Override
	public void onConstantPoolInvokeDynamic(int index, int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
		System.out.println("ConstantPool " + index + " InvokeDynamic");
		
		if (delegate != null) {
			delegate.onConstantPoolInvokeDynamic(index, bootstrapMethodAttrIndex, nameAndTypeIndex);
		}
	}

	private static String hex16(int value) {
		return String.format("0x%04x", value);
	}
	
	@Override
	public void onClassInfo(int accessFlags, int thisClass, int superClass) {
		System.out.format("ClassInfo 0x%04x %d %d\n", accessFlags, thisClass, superClass);
		
		if (delegate != null) {
			delegate.onClassInfo(accessFlags, thisClass, superClass);
		}
	}

	@Override
	public void onInterfacesCount(int count) {

		System.out.println("InterfacesCount " + count);
		
		if (delegate != null) {
			delegate.onInterfacesCount(count);
		}
	}

	@Override
	public void onInterface(int index, int nameIndex) {

		System.out.println("Interface " + index + " " + nameIndex);
		
		if (delegate != null) {
			delegate.onInterface(index, nameIndex);
		}
	}

	@Override
	public void onFieldCount(int count) {

		System.out.println("FieldCount " + count);

		if (delegate != null) {
			delegate.onFieldCount(count);
		}
	}

	@Override
	public void onField(int index, int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {

		System.out.println("Field " + index + " " + hex16(accessFlags) + " " + nameIndex + " " + descriptorIndex + " " + attributesCount);
		
		if (delegate != null) {
			delegate.onField(index, accessFlags, nameIndex, descriptorIndex, attributesCount);
		}
	}

	@Override
	public void onFieldAttribute(int fieldIndex, int attributeIndex, int attributeNameIndex, int attributeLength,
			DataInput dataInput) throws IOException {
		
		System.out.println("FieldAttribute " + fieldIndex + " " + attributeIndex + " "
					+ attributeNameIndex + " " + attributeLength);

		
		if (delegate != null) {
			delegate.onFieldAttribute(fieldIndex, attributeIndex, attributeNameIndex, attributeLength, dataInput);
		}
		else {
			dataInput.skipBytes(attributeLength);
		}
	}

	@Override
	public void onMethodCount(int count) {

		System.out.println("MethodCount " + count);
		
		
		if (delegate != null) {
			delegate.onMethodCount(count);
		}
	}

	@Override
	public void onMethod(int index, int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {

		System.out.println("Method " + index + " " + hex16(accessFlags) + " " + nameIndex + " " + descriptorIndex + " " + attributesCount);
	
		
		if (delegate != null) {
			delegate.onMethod(index, accessFlags, nameIndex, descriptorIndex, attributesCount);
		}
	}

	@Override
	public void onMethodAttribute(int methodIndex, int attributeIndex, int attributeNameIndex, int attributeLength,
			DataInput dataInput) throws IOException {

		System.out.println("MethodAttribute " + methodIndex + " " + attributeIndex + " "
				+ attributeNameIndex + " " + attributeLength);
		
		if (delegate != null) {
			delegate.onMethodAttribute(methodIndex, attributeIndex, attributeNameIndex, attributeLength, dataInput);
		}
		else {
			dataInput.skipBytes(attributeLength);
		}
	}

	@Override
	public void onClassFileAttributeCount(int count) {

		System.out.println("ClassFileAttributeCount " + count);
		
		if (delegate != null) {
			delegate.onClassFileAttributeCount(count);
		}
	}

	@Override
	public void onClassFileAttribute(int attributeIndex, int attributeNameIndex, int attributeLength,
			DataInput dataInput) throws IOException {

		System.out.println("ClassFileAttribute " + attributeIndex + " "
				+ attributeNameIndex + " " + attributeLength);
		
		if (delegate != null) {
			delegate.onClassFileAttribute(attributeIndex, attributeNameIndex, attributeLength, dataInput);
		}
		else {
			dataInput.skipBytes(attributeLength);
		}
	}

}
