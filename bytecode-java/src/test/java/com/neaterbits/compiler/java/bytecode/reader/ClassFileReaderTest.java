package com.neaterbits.compiler.java.bytecode.reader;

import java.io.DataInput;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

import com.neaterbits.compiler.bytecode.common.ClassFileException;
import com.neaterbits.compiler.java.bytecode.reader.ClassFileReader;
import com.neaterbits.compiler.java.bytecode.reader.ClassFileReaderListener;

public class ClassFileReaderTest {

	@Test
	public void testClassFileLoading() throws IOException, ClassFileException {
	
		final File file = new File("target/classes/com/neaterbits/compiler/java/bytecode/ClassFileReader.class");

		try (FileInputStream inputStream = new FileInputStream(file)) {

			ClassFileReader.readClassFile(inputStream, new ReaderListener());
		}
	}
	
	private static class ReaderListener implements ClassFileReaderListener {

		@Override
		public void onConstantPoolCount(int count) {
			System.out.println("ConstantPoolCount " + count);
		}

		@Override
		public void onConstantPoolClass(int index, int nameIndex) {
			System.out.println("ConstantPool " + index + " Class");
		}

		@Override
		public void onConstantPoolFieldref(int index, int classIndex, int nameAndTypeIndex) {
			System.out.println("ConstantPool " + index + " Fieldref");
		}

		@Override
		public void onConstantPoolMethodref(int index, int classIndex, int nameAndTypeIndex) {
			System.out.println("ConstantPool " + index + " Methodref");
		}

		@Override
		public void onConstantPoolInterfaceMethodref(int index, int classIndex, int nameAndTypeIndex) {
			System.out.println("ConstantPool " + index + " InterfaceMethodref");
		}

		@Override
		public void onConstantPoolString(int index, int stringIndex) {
			System.out.println("ConstantPool " + index + " String");
		}

		@Override
		public void onConstantPoolInteger(int index, int value) {
			System.out.println("ConstantPool " + index + " Integer");
		}

		@Override
		public void onConstantPoolFloat(int index, float value) {
			System.out.println("ConstantPool " + index + " Float");
		}

		@Override
		public void onConstantPoolLong(int index, long value) {
			System.out.println("ConstantPool " + index + " Long");
		}

		@Override
		public void onConstantPoolDouble(int index, double value) {
			System.out.println("ConstantPool " + index + " Double");
		}

		@Override
		public void onConstantPoolNameAndType(int index, int nameIndex, int descriptorIndex) {
			System.out.println("ConstantPool " + index + " NameAndType");
		}

		@Override
		public void onConstantPoolUTF8(int index, String value) {
			System.out.println("ConstantPool " + index + " UTF8 " + value);
		}

		@Override
		public void onConstantPoolMethodHandle(int index, int referenceKind, int referenceIndex) {
			System.out.println("ConstantPool " + index + " MethodHandlke");
		}

		@Override
		public void onConstantPoolMethodType(int index, int descriptorIndex) {
			System.out.println("ConstantPool " + index + " MethodType");
		}

		@Override
		public void onConstantPoolInvokeDynamic(int index, int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
			System.out.println("ConstantPool " + index + " InvokeDynamic");
		}

		@Override
		public void onClassInfo(int accessFlags, int thisClass, int superClass) {
			System.out.format("ClassInfo 0x%04x %d %d", accessFlags, thisClass, superClass);
		}

		@Override
		public void onInterfacesCount(int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onInterface(int index, int nameIndex) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFieldCount(int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onField(int index, int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onFieldAttribute(int fieldIndex, int attributeIndex, int attributeNameIndex, int attributeLength,
				DataInput dataInput) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMethodCount(int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMethod(int index, int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMethodAttribute(int methodIndex, int attributeIndex, int attributeNameIndex, int attributeLength,
				DataInput dataInput) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onClassFileAttributeCount(int count) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onClassFileAttribute(int attributeIndex, int attributeNameIndex, int attributeLength,
				DataInput dataInput) {
			// TODO Auto-generated method stub
			
		}
	}
}
