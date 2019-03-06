package com.neaterbits.compiler.java.bytecode.reader;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.compiler.bytecode.common.ClassFileException;

public class ClassFileReader {
	
	public static void readClassFile(InputStream inputStream, ClassFileReaderListener readerListener) throws IOException, ClassFileException {

		final DataInputStream dataInput = new DataInputStream(inputStream);
		
		dataInput.readInt();
		
		dataInput.readShort();
		dataInput.readShort();
		
		readConstantPool(dataInput, readerListener);
		
		readerListener.onClassInfo(
				dataInput.readUnsignedShort(),
				dataInput.readUnsignedShort(),
				dataInput.readUnsignedShort());
		
		readInterfaces(dataInput, readerListener);
		
		readFields(dataInput, readerListener);
		
		readMethods(dataInput, readerListener);
	}

	private static void readConstantPool(DataInputStream dataInput, ClassFileReaderListener readerListener) throws IOException, ClassFileException {
		
		final int count = dataInput.readUnsignedShort() - 1;

		readerListener.onConstantPoolCount(count);
		
		for (int i = 0; i < count; ++ i) {
		
			final byte tag = dataInput.readByte();
			
			final ConstantPoolTag ctp = ConstantPoolTag.getConstantPoolTag(tag);
			
			if (ctp == null) {
				throw new ClassFileException("Unknown constant pool tag " + tag);
			}
			
			switch (ctp) {
			case CLASS: {
				final int nameIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolClass(i, nameIndex);
				break;
			}
				
			case FIELDREF: {
				final int classIndex = dataInput.readUnsignedShort();
				final int nameAndTypeIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolFieldref(i, classIndex, nameAndTypeIndex);
				break;
			}

			case METHODREF: {
				final int classIndex = dataInput.readUnsignedShort();
				final int nameAndTypeIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolMethodref(i, classIndex, nameAndTypeIndex);
				break;
			}

			case INTERFACE_METHODREF: {
				final int classIndex = dataInput.readUnsignedShort();
				final int nameAndTypeIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolInterfaceMethodref(i, classIndex, nameAndTypeIndex);
				break;
			}

			case STRING:
				final int stringIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolString(i, stringIndex);
				break;
				
			case INTEGER: {
				
				final int value = dataInput.readInt();
				
				readerListener.onConstantPoolInteger(i, value);
				break;
			}
			
			case FLOAT: {
				
				final float value = dataInput.readFloat();
				
				readerListener.onConstantPoolFloat(i, value);
				break;
			}
				
			case LONG: {
				
				final long value = dataInput.readLong();
				
				readerListener.onConstantPoolLong(i, value);
				break;
			}
			
			case DOUBLE: {
				
				final double value = dataInput.readDouble();
				
				readerListener.onConstantPoolDouble(i, value);
				break;
			}
			
			case NAME_AND_TYPE: {
				final int nameIndex = dataInput.readUnsignedShort();
				final int descriptorIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolNameAndType(i, nameIndex, descriptorIndex);
				break;
			}
			
			case UTF8:
				final String string = dataInput.readUTF();
				
				readerListener.onConstantPoolUTF8(i, string);
				break;
				
			case METHOD_HANDLE: {
				final int referenceKind = dataInput.readUnsignedByte();
				final int referenceIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolMethodHandle(i, referenceKind, referenceIndex);
				break;
			}
				
			case METHOD_TYPE: {
				final int descriptorIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolMethodType(i, descriptorIndex);
				break;
			}
			
			case INVOKE_DYNAMIC: {
				final int bootstrapMethodAttrIndex = dataInput.readUnsignedShort();
				final int nameAndTypeIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolInvokeDynamic(i, bootstrapMethodAttrIndex, nameAndTypeIndex);
				break;
			}
			
			default:
				throw new UnsupportedOperationException();
			}
		}
	}
	

	private static void readInterfaces(DataInputStream dataInput, ClassFileReaderListener readerListener) throws IOException {
		
		final int interfacesCount = dataInput.readUnsignedShort();
		
		readerListener.onInterfacesCount(interfacesCount);
		
		for (int i = 0; i < interfacesCount; ++ i) {
			readerListener.onInterface(i, dataInput.readUnsignedShort());
		}
	}
	
	private static void readFields(DataInputStream dataInput, ClassFileReaderListener readerListener) throws IOException {
		
		final int fieldCount = dataInput.readUnsignedShort();
		
		readerListener.onFieldCount(fieldCount);
		
		for (int i = 0; i < fieldCount; ++ i) {
			final int accessFlags = dataInput.readUnsignedShort();
			final int nameIndex = dataInput.readUnsignedShort();
			final int descriptorIndex = dataInput.readUnsignedShort();
			final int attributesCount = dataInput.readUnsignedShort();
			
			readerListener.onField(i, accessFlags, nameIndex, descriptorIndex, attributesCount);
		
			final int fieldIndex = i;
			
			readAttributes(dataInput, readerListener, attributesCount,
					(attributeIndex, attributeNameIndex, attributeLength)
						-> readerListener.onFieldAttribute(fieldIndex, attributeIndex, attributeNameIndex, attributeLength, dataInput));
		}
	}
	
	@FunctionalInterface
	interface OnAttribute {
		void onAttribute(int attributeIndex, int nameIndex, int attributesLength) throws IOException;
	}
	
	private static void readMethods(DataInputStream dataInput, ClassFileReaderListener readerListener) throws IOException {
		
		final int methodCount = dataInput.readUnsignedShort();
		
		readerListener.onMethodCount(methodCount);
		
		for (int i = 0; i < methodCount; ++ i) {
			final int accessFlags = dataInput.readUnsignedShort();
			final int nameIndex = dataInput.readUnsignedShort();
			final int descriptorIndex = dataInput.readUnsignedShort();
			final int attributesCount = dataInput.readUnsignedShort();
			
			readerListener.onField(i, accessFlags, nameIndex, descriptorIndex, attributesCount);
		
			final int methodIndex = i;
			
			readAttributes(dataInput, readerListener, attributesCount,
					(attributeIndex, attributeNameIndex, attributeLength)
						-> readerListener.onMethodAttribute(methodIndex, attributeIndex, attributeNameIndex, attributeLength, dataInput));
		}
	}

	private static void readAttributes(
			DataInputStream dataInput,
			ClassFileReaderListener readerListener,
			int attributesCount,
			OnAttribute onAttribute
			) throws IOException {
		
		for (int i = 0; i < attributesCount; ++ i) {
			
			final int nameIndex = dataInput.readUnsignedShort();
			final int attributesLength = dataInput.readInt();
			
			onAttribute.onAttribute(i, nameIndex, attributesLength);
			
		}
	}

	public static void parseAttribute(
				String constant,
				int memberIndex,
				int attributeLength,
				DataInput dataInput,
				ClassFileAttributesListener attributesListener) throws IOException {
		
		switch (constant) {
		case "ConstantValue":
			attributesListener.onConstantValue(memberIndex, dataInput.readUnsignedShort());
			break;
			
		case "Code":
			attributesListener.onCode(memberIndex, attributeLength, dataInput);
			break;
			
		case "StackMapTable":
			attributesListener.onStackMapTable(memberIndex, attributeLength, dataInput);
			break;
			
		case "Exceptions":
			attributesListener.onExceptions(memberIndex, attributeLength, dataInput);
			break;
			
		case "LineNumberTable":
		case "INDENT":
			dataInput.skipBytes(attributeLength);
			break;
		
		default:
			throw new UnsupportedOperationException("Unknown attribute " + constant);
		}
	}
}
