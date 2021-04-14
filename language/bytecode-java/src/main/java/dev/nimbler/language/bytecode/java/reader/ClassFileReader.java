package dev.nimbler.language.bytecode.java.reader;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import dev.nimbler.language.bytecode.common.ClassFileException;

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

			final int index = i + 1;

			final byte tag = dataInput.readByte();

			final ConstantPoolTag ctp = ConstantPoolTag.getConstantPoolTag(tag);
			
			if (ctp == null) {
				throw new ClassFileException("Unknown constant pool tag " + tag);
			}
			
			switch (ctp) {
			case CLASS: {
				final int nameIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolClass(index, nameIndex);
				break;
			}
				
			case FIELDREF: {
				final int classIndex = dataInput.readUnsignedShort();
				final int nameAndTypeIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolFieldref(index, classIndex, nameAndTypeIndex);
				break;
			}

			case METHODREF: {
				final int classIndex = dataInput.readUnsignedShort();
				final int nameAndTypeIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolMethodref(index, classIndex, nameAndTypeIndex);
				break;
			}

			case INTERFACE_METHODREF: {
				final int classIndex = dataInput.readUnsignedShort();
				final int nameAndTypeIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolInterfaceMethodref(index, classIndex, nameAndTypeIndex);
				break;
			}

			case STRING:
				final int stringIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolString(index, stringIndex);
				break;
				
			case INTEGER: {
				
				final int value = dataInput.readInt();
				
				readerListener.onConstantPoolInteger(index, value);
				break;
			}
			
			case FLOAT: {
				
				final float value = dataInput.readFloat();
				
				readerListener.onConstantPoolFloat(index, value);
				break;
			}
				
			case LONG: {
				
				final long value = dataInput.readLong();
				
				readerListener.onConstantPoolLong(index, value);
				
				++ i;
				break;
			}
			
			case DOUBLE: {
				
				final double value = dataInput.readDouble();
				
				readerListener.onConstantPoolDouble(index, value);
				
				++ i;
				break;
			}
			
			case NAME_AND_TYPE: {
				final int nameIndex = dataInput.readUnsignedShort();
				final int descriptorIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolNameAndType(index, nameIndex, descriptorIndex);
				break;
			}
			
			case UTF8:
				final String string = dataInput.readUTF();
				
				readerListener.onConstantPoolUTF8(index, string);
				break;
				
			case METHOD_HANDLE: {
				final int referenceKind = dataInput.readUnsignedByte();
				final int referenceIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolMethodHandle(index, referenceKind, referenceIndex);
				break;
			}
				
			case METHOD_TYPE: {
				final int descriptorIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolMethodType(index, descriptorIndex);
				break;
			}
			
			case INVOKE_DYNAMIC: {
				final int bootstrapMethodAttrIndex = dataInput.readUnsignedShort();
				final int nameAndTypeIndex = dataInput.readUnsignedShort();
				
				readerListener.onConstantPoolInvokeDynamic(index, bootstrapMethodAttrIndex, nameAndTypeIndex);
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
			
			readAttributes(dataInput, attributesCount,
					(attributeIndex, attributeNameIndex, attributeLength)
						-> readerListener.onFieldAttribute(fieldIndex, attributeIndex, attributeNameIndex, attributeLength, dataInput));
		}
	}
	
	private static void readMethods(DataInputStream dataInput, ClassFileReaderListener readerListener) throws IOException {
		
		final int methodCount = dataInput.readUnsignedShort();
		
		readerListener.onMethodCount(methodCount);
		
		for (int i = 0; i < methodCount; ++ i) {
			final int accessFlags = dataInput.readUnsignedShort();
			final int nameIndex = dataInput.readUnsignedShort();
			final int descriptorIndex = dataInput.readUnsignedShort();
			final int attributesCount = dataInput.readUnsignedShort();
			
			readerListener.onMethod(i, accessFlags, nameIndex, descriptorIndex, attributesCount);
		
			final int methodIndex = i;
			
			readAttributes(dataInput, attributesCount,
					(attributeIndex, attributeNameIndex, attributeLength)
						-> readerListener.onMethodAttribute(methodIndex, attributeIndex, attributeNameIndex, attributeLength, dataInput));
		}
	}

	public static void readAttributes(DataInput dataInput, int attributesCount, OnAttribute onAttribute) throws IOException {
		
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
			//dataInput.skipBytes(attributeLength);
			attributesListener.onCode(memberIndex, attributeLength, dataInput);
			break;
			
		case "StackMapTable":
			attributesListener.onStackMapTable(memberIndex, attributeLength, dataInput);
			break;
			
		case "Exceptions":
			attributesListener.onExceptions(memberIndex, attributeLength, dataInput);
			break;

		case "LocalVariableTable":
			attributesListener.onLocalVariableTable(memberIndex, attributeLength, dataInput);
			break;
		
		case "RuntimeVisibleAnnotations":
			attributesListener.onRuntimeVisibleAnnotations(memberIndex, attributeLength, dataInput);
			break;
			
		case "Signature":
			attributesListener.onSignature(memberIndex, dataInput.readUnsignedShort());
			break;
			
		case "Deprecated":
			attributesListener.onDeprecated(memberIndex);
			break;

		case "AnnotationDefault":
			attributesListener.onAnnotationDefault(memberIndex, attributeLength, dataInput);
			break;

		case "RuntimeInvisibleAnnotations":
			attributesListener.onRuntimeInvisibleAnnotations(memberIndex, attributeLength, dataInput);
			break;

		case "RuntimeInvisibleParameterAnnotations":
			attributesListener.onRuntimeInvisibleParameterAnnotations(memberIndex, attributeLength, dataInput);
			break;
		
		case "Synthetic":
			attributesListener.onSynthetic(memberIndex);
			break;
			
		default:
			throw new UnsupportedOperationException("Unknown attribute " + constant);

		}
	}
}
