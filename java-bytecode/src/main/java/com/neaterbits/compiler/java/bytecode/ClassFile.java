package com.neaterbits.compiler.java.bytecode;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import com.neaterbits.compiler.bytecode.common.BaseClassFile;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.bytecode.common.MethodClassReferenceScanner;
import com.neaterbits.compiler.bytecode.common.MethodType;
import com.neaterbits.compiler.common.FieldType;
import com.neaterbits.compiler.common.TypeName;
import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.BaseTypeName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.DefinitionName;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.codemap.MethodVariant;
import com.neaterbits.compiler.common.util.Strings;
import com.neaterbits.compiler.common.util.ValueMap;
import com.neaterbits.compiler.java.bytecode.reader.ClassFileAttributesListener;
import com.neaterbits.compiler.java.bytecode.reader.ClassFileReader;
import com.neaterbits.compiler.java.bytecode.reader.ClassFileReaderListener;
import com.neaterbits.compiler.java.bytecode.reader.ConstantPoolTag;

import static com.neaterbits.compiler.java.bytecode.JavaBytecodes.*;

class ClassFile extends BaseClassFile implements ClassBytecode, ClassFileReaderListener {

	private ValueMap constantPool;
	
	private int numConstantPoolStrings;
	private String [] constantPoolStrings;
	
	private int accessFlags;
	private int thisClass;
	private int superClass;

	private int [] interfaces;
	
	private Field [] fields;
	private Method [] methods;
	
	final String getUTF8(int index) {
		return constantPoolStrings[(int)getValueInConstantPool(index, ConstantPoolTag.UTF8)];
	}
	
	final int getAccessFlags() {
		return accessFlags;
	}
	
	final int[] getInterfaces() {
		return interfaces;
	}

	final Field[] getFields() {
		return fields;
	}

	final Method[] getMethods() {
		return methods;
	}

	final int getThisClass() {
		return thisClass;
	}

	@Override
	public TypeVariant getTypeVariant() {
		
		final TypeVariant typeVariant;
		
		if ((accessFlags & AccessFlags.ACC_INTERFACE) != 0) {
			typeVariant = TypeVariant.INTERFACE;
		}
		else if ((accessFlags & AccessFlags.ACC_ENUM) != 0) {
			typeVariant = TypeVariant.ENUM;
		}
		else {
			typeVariant = TypeVariant.CLASS;
		}
		
		return typeVariant;
	}

	final int getSuperClassIndex() {
		return superClass;
	}

	@Override
	public final TypeName getSuperClass() {
		return getTypeName(superClass);
	}
	
	
	@Override
	public int getFieldCount() {
		return fields.length;
	}

	@Override
	public String getFieldName(int fieldIdx) {
		return getUTF8(fieldIdx);
	}

	@Override
	public FieldType getFieldType(int fieldIdx) {
		return getFieldType(fields[fieldIdx].getDescriptorIndex());
	}
	
	@Override
	public boolean isStatic(int fieldIdx) {
		return (fields[fieldIdx].getAccessFlags() & AccessFlags.ACC_STATIC) != 0;
	}

	private static int getEncodedTypeLength(String encodedTypes, int index) {

		final char c = encodedTypes.charAt(index);
		final int encodedTypeLength;
		
		switch (c) {
		case 'B':
		case 'C':
		case 'D':
		case 'F':
		case 'I':
		case 'J':
		case 'S':
		case 'Z':
			encodedTypeLength = 1;
			break;
			
		case 'L':
			final int classNameEndIndex = encodedTypes.indexOf(";", index + 1);
			
			if (classNameEndIndex < 0) {
				throw new IllegalArgumentException();
			}
			encodedTypeLength = classNameEndIndex + 1;
			break;
			
		case '[':
			encodedTypeLength = getEncodedTypeLength(encodedTypes, index + 1);
			break;
			
		default:
			throw new IllegalStateException("Unknown type code " + c);
		}

		return encodedTypeLength;
	}

	private int getMethodIdx(String methodName, List<FieldType> parameterTypes) {
		
		Objects.requireNonNull(methodName);
		Objects.requireNonNull(parameterTypes);
		
		int methodIdx = -1;
		
		for (int i = 0; i < methods.length; ++ i) {
			
			final Method method = methods[i];
			
			final String methodNameFromMethod = getUTF8(method.getNameIndex());

			if (methodName.equals(methodNameFromMethod)) {
				final List<TypeName> parameterTypesFromMethod = new ArrayList<>();

				getMethodDescriptorTypes(
						method.getDescriptorIndex(),
						parameterType -> parameterTypesFromMethod.add(getTypeName(parameterType)));
				
				if (parameterTypes.equals(parameterTypesFromMethod)) {
					methodIdx = i;
					break;
				}
			}
		}
		
		return methodIdx;
	}

	@Override
	public final int getMethodIndex(String methodName, List<FieldType> parameterTypes) {
		return getMethodIdx(methodName, parameterTypes);
	}

	@Override
	public final byte [] getMethodBytecode(int methodIdx) {

		final byte [] bytecode = methods[methodIdx].getBytecode();

		return bytecode;
	}
	
	@Override
	public final int getMethodMaxOperandStack(int methodIdx) {
		return methods[methodIdx].getMaxStack();
	}

	@Override
	public final int getMethodMaxLocals(int methodIdx) {
		return methods[methodIdx].getMaxLocals();
	}

	@Override
	public final int getMethodCount() {
		return methods.length;
	}

	@Override
	public final String getMethodName(int methodIdx) {
		return getUTF8(methods[methodIdx].getNameIndex());
	}

	@Override
	public final FieldType getMethodSignature(int methodIdx, List<FieldType> parameterTypes) {
		
		final String returnType = getMethodDescriptorTypes(
				methods[methodIdx].getDescriptorIndex(),
				parameterType -> parameterTypes.add(getFieldType(parameterType)));

		return getFieldType(returnType);
	}
	
	@Override
	public FieldType getMethodReturnType(int methodIdx) {
		final String descriptor = getUTF8(methods[methodIdx].getDescriptorIndex());
		
		final int paramsEndIndex = descriptor.indexOf(")", 1);
		if (paramsEndIndex < 0) {
			throw new IllegalArgumentException();
		}
		
		final String returnTypeString = descriptor.substring(paramsEndIndex + 1);

		return getFieldType(returnTypeString);
	}

	@Override
	public final MethodVariant getMethodVariant(int methodIdx) {
	
		final int accessFlags = methods[methodIdx].getAccessFlags();
		
		final MethodVariant methodVariant;
		
		if ((accessFlags & AccessFlags.ACC_STATIC) != 0) {
			
			methodVariant = MethodVariant.STATIC;
			
		} else if ((accessFlags & AccessFlags.ACC_ABSTRACT) != 0) {
			
			methodVariant = MethodVariant.ABSTRACT;
		
		} else if ((accessFlags & AccessFlags.ACC_FINAL) != 0) {
		
			methodVariant = MethodVariant.FINAL_IMPLEMENTATION;
		
		}
		else {
			methodVariant = MethodVariant.OVERRIDABLE_IMPLEMENTATION;
		}
		
		return methodVariant;
	}

	private <T> void onMethodInvocation(byte [] bytecode, int idx, MethodType methodType, MethodClassReferenceScanner<T> scanner, T param) {

		final int byteindex1 = bytecode[idx];
		final int byteindex2 = bytecode[idx + 1];
		
		final int methodReferenceIdx = byteindex1 << 8 | byteindex2;
		
		final long methodReference = getValueInConstantPool(methodReferenceIdx);
		
		final int classNameIndex = (int)(methodReference >> 32);
		
		final int nameAndTypeInfoIdx = (int)(methodReference & 0xFFFFFFFFL);
		
		final long nameAndTypeInfo = getValueInConstantPool(nameAndTypeInfoIdx);
		
		final int methodNameIdx = (int)(nameAndTypeInfo >> 32);
		
		final int methodDescriptorIdx = (int)(nameAndTypeInfoIdx & 0xFFFFFFFFL);
		
		final String methodName = getUTF8(methodNameIdx);
		final TypeName typeName = getTypeName(classNameIndex);
		
		final List<FieldType> parameterTypes = new ArrayList<>();
		
		final String returnType = getMethodDescriptorTypes(
				methodDescriptorIdx,
				parameterType -> parameterTypes.add(getFieldType(parameterType)));
		
		scanner.onMethodInvocation(
				param,
				typeName,
				methodName,
				getFieldType(returnType),
				parameterTypes,
				methodType);
	}
	
	@Override
	public final <T> void scanMethodClassReferences(int methodIdx, MethodClassReferenceScanner<T> scanner, T param) {

		final byte [] bytecode = methods[methodIdx].getBytecode();
		
		for (int idx = 0; idx < bytecode.length;) {
			switch ((int)bytecode[idx]) {
			
			case ALOAD:		++ idx;		break;
			case ANEWARRAY:	idx += 2;	break;
			case ASTORE:	++ idx;		break;
			case BIPUSH:	++ idx;		break;
			case CHECKCAST: idx += 2;	break;
			case DLOAD:		++ idx;		break;
			case DSTORE:	++ idx;		break;
			case FLOAD:		++ idx;		break;
			case FSTORE:	++ idx;		break;
			case GETFIELD:	idx += 2;	break;
			case GETSTATIC:	idx += 2;	break;
			case GOTO:		idx += 2;	break;
			case GOTO_W:	idx += 4;	break;

			case IF_ACMPEQ:	idx += 2;	break;
			case IF_ACMPNE:	idx += 2;	break;
			case IF_ICMPEQ:	idx += 2;	break;
			case IF_ICMPGE:	idx += 2;	break;
			case IF_ICMPGT:	idx += 2;	break;
			case IF_ICMPLE:	idx += 2;	break;
			case IF_ICMPLT:	idx += 2;	break;
			case IF_ICMPNE:	idx += 2;	break;
			case IFEQ:		idx += 2;	break;
			case IFGE:		idx += 2;	break;
			case IFGT:		idx += 2;	break;
			case IFLE:		idx += 2;	break;
			case IFLT:		idx += 2;	break;
			case IFNE:		idx += 2;	break;
			case IFNONNULL:	idx += 2;	break;
			case IFNULL:	idx += 2;	break;

			case ILOAD:		++ idx;		break;

			case INSTANCEOF:	idx += 2;	break;

			case INVOKEDYNAMIC:
				onMethodInvocation(bytecode, idx, MethodType.DYNAMIC, scanner, param);
				idx += 4;
				break;

			case INVOKEINTERFACE:
				onMethodInvocation(bytecode, idx, MethodType.INTERFACE, scanner, param);
				idx += 4;
				break;

			case INVOKESPECIAL:
				onMethodInvocation(bytecode, idx, MethodType.METHOD, scanner, param);
				idx += 2;
				break;

			case INVOKESTATIC:
				onMethodInvocation(bytecode, idx, MethodType.STATIC, scanner, param);
				idx += 2;
				break;
			
			case INVOKEVIRTUAL:
				onMethodInvocation(bytecode, idx, MethodType.VIRTUAL, scanner, param);
				idx += 2;
				break;

			case ISTORE:	++ idx;		break;

			case JSR:		idx += 2;	break;
			case JSR_W:		idx += 4;	break;

			case LDC:		++ idx;		break;
			case LDC_W:		idx += 2;	break;
			case LDC2_W:	idx += 2;	break;

			case LLOAD:		++ idx;		break;
			case LSTORE:	++ idx;		break;

			case MULTIANEWARRAY:
				idx += 3;
				break;
			case NEW:		idx += 2;	break;
			case NEWARRAY:	++ idx;		break;

			case PUTFIELD:	idx += 2;	break;
			case PUTSTATIC:	idx += 2;	break;
			
			case RET:		++ idx;		break;

			case SIPUSH:	idx += 2;	break;

			default:
				++ idx;
				break;
			}
		}
	}
	
	TypeName getReferencedTypeName(int index) {
		throw new UnsupportedOperationException("TODO");
	}

	final CompleteName getCompleteName(int classIndex, Function<String, BaseTypeName> createTypeName) {
		
		final TypeName typeName = getTypeName(classIndex);
		
		final List<DefinitionName> outerTypes;
		
		if (typeName.getOuterTypes() != null) {
			outerTypes = new ArrayList<>(typeName.getOuterTypes().length);
		
			for (String outerType : typeName.getOuterTypes()) {
				outerTypes.add(new ClassName(outerType));
			}
		}
		else {
			outerTypes = null;
		}
		
		return new CompleteName(
				new NamespaceReference(typeName.getNamespace()),
				outerTypes,
				createTypeName.apply(typeName.getName()));
	}
		
	final TypeName getTypeName(int classIndex) {
		final String name = getUTF8((int)getValueInConstantPool(getThisClass(), ConstantPoolTag.CLASS));
	
		return getTypeName(name);
	}
	
	final TypeName getTypeName(String name) {
		final String [] parts = Strings.split(name, '.');
	
		final String [] namespaceReference = Arrays.copyOf(parts, parts.length - 1);
	
		final String [] classes = Strings.split(parts[parts.length - 1], '$');
		
		final String [] outerTypes;
		
		if (classes.length > 1) {
			outerTypes = new String[classes.length - 1];
			
			for (int i = 0; i < classes.length - 1; ++ i) {
				
				outerTypes[i] = classes[i];
			}
		}
		else {
			outerTypes = null;
		}
		
		return new TypeName(namespaceReference, outerTypes, classes[classes.length - 1]);
	}
	
	private FieldType getFieldType(String encodedType) {
		
		int idx;
		
		int numArrayDimensions = 0;

		for (idx = 0; encodedType.charAt(idx) == '['; ++ idx) {
			++ numArrayDimensions;
		}
		
		String [] namespace = null;
		String [] outerTypes = null;
		boolean builtin = true;
		
		final String name;
		final int sizeInBits;
		
		final char typeCode = encodedType.charAt(idx);
		
		switch (typeCode) {
		case 'B':	name = "byte";		sizeInBits = 8; 	break;
		case 'C':	name = "char";		sizeInBits = 16;	break;
		case 'D':	name = "double";	sizeInBits = 64;	break;
		case 'F':	name = "float";		sizeInBits = 32;	break;
		case 'I':	name = "integer";	sizeInBits = 32;	break;
		case 'J':	name = "long";		sizeInBits = 64; 	break;
		case 'S':	name = "short";		sizeInBits = 16;    break;
		case 'Z':	name = "boolean";	sizeInBits = 1;		break;
			
		case 'L':
			final int endOfClassName = encodedType.indexOf(";", idx + 1);
			
			if (endOfClassName < 0) {
				throw new IllegalStateException();
			}
			
			final TypeName typeName = getTypeName(encodedType.substring(idx + 1, endOfClassName));
			
			namespace = typeName.getNamespace();
			outerTypes = typeName.getOuterTypes();
			name = typeName.getName();

			builtin = false;
			sizeInBits = -1;
			break;

		default:
			throw new IllegalStateException("Unknown typecode " + typeCode);
		}
		
		return new FieldType(namespace, outerTypes, name, builtin, sizeInBits, numArrayDimensions);
	}
	
	final String getMethodDescriptorTypes(int methodDescriptorIdx, Consumer<String> onParameter) {
		final String descriptor = getUTF8(methodDescriptorIdx);
		
		if (descriptor.charAt(0) != '(') {
			throw new IllegalArgumentException();
		}
		
		final int paramsEndIndex = descriptor.indexOf(")", 1);
		if (paramsEndIndex < 0) {
			throw new IllegalArgumentException();
		}
		
		final String encodedParamTypes = descriptor.substring(1, paramsEndIndex);

		getReferenceList(encodedParamTypes, onParameter);
		
		return descriptor.substring(paramsEndIndex + 1);
	}

	private static void getReferenceList(String encodedTypes, Consumer<String> onParameter) {
		
		int index = 0;
		
		while (index < encodedTypes.length()) {

			final int encodedTypeLength = getEncodedTypeLength(encodedTypes, index);
			
			final String encodedType = encodedTypes.substring(index, index + encodedTypeLength);
			
			index += encodedTypeLength;

			onParameter.accept(encodedType);
		}
	}

	@Override
	public final void onConstantPoolCount(int count) {
		if (constantPool != null) {
			throw new IllegalStateException();
		}
		
		this.constantPool = new ValueMap(68, count);
	}

	private void storeInConstantPool(int index, ConstantPoolTag tag, long value) {
		constantPool.storeValue(index, 0, 4, tag.ordinal());
		constantPool.storeValue(index, 4, 64, value);
	}

	private ConstantPoolTag getTagInConstantPool(int index) {
		return ConstantPoolTag.values()[(int)constantPool.getValue(index, 0, 4)];
	}

	private long getValueInConstantPool(int index) {
		return constantPool.getValue(index, 4, 64);
	}
	
	final long getValueInConstantPool(int index, ConstantPoolTag tag) {
		if (tag != getTagInConstantPool(index)) {
			throw new IllegalStateException();
		}
		
		return getValueInConstantPool(index);
	}
		
	@Override
	public final void onConstantPoolClass(int index, int nameIndex) {
		storeInConstantPool(index, ConstantPoolTag.CLASS, nameIndex);
	}

	@Override
	public final void onConstantPoolFieldref(int index, int classIndex, int nameAndTypeIndex) {
		storeInConstantPool(index, ConstantPoolTag.FIELDREF, classIndex << 32 | nameAndTypeIndex);
	}

	@Override
	public final void onConstantPoolMethodref(int index, int classIndex, int nameAndTypeIndex) {
		storeInConstantPool(index, ConstantPoolTag.METHODREF, classIndex << 32 | nameAndTypeIndex);
	}

	@Override
	public final void onConstantPoolInterfaceMethodref(int index, int classIndex, int nameAndTypeIndex) {
		storeInConstantPool(index, ConstantPoolTag.INTERFACE_METHODREF, classIndex << 32 | nameAndTypeIndex);
	}

	@Override
	public final void onConstantPoolString(int index, int stringIndex) {
		storeInConstantPool(index, ConstantPoolTag.STRING, stringIndex);
	}

	@Override
	public final void onConstantPoolInteger(int index, int value) {
		storeInConstantPool(index, ConstantPoolTag.INTEGER, value);
	}

	@Override
	public final void onConstantPoolFloat(int index, float value) {
		storeInConstantPool(index, ConstantPoolTag.FLOAT, Float.floatToIntBits(value));
	}

	@Override
	public final void onConstantPoolLong(int index, long value) {
		storeInConstantPool(index, ConstantPoolTag.LONG, value);
	}

	@Override
	public final void onConstantPoolDouble(int index, double value) {
		storeInConstantPool(index, ConstantPoolTag.DOUBLE, Double.doubleToLongBits(value));
	}

	@Override
	public final void onConstantPoolNameAndType(int index, int nameIndex, int descriptorIndex) {
		storeInConstantPool(index, ConstantPoolTag.NAME_AND_TYPE, nameIndex << 32 | descriptorIndex);
	}

	@Override
	public final void onConstantPoolUTF8(int index, String value) {
		
		if (constantPoolStrings == null) {
			constantPoolStrings = new String[50];
		}
		else if (numConstantPoolStrings == constantPoolStrings.length) {
			constantPoolStrings = Arrays.copyOf(constantPoolStrings, constantPoolStrings.length * 3);
		}
		
		constantPoolStrings[numConstantPoolStrings] = value;
		
		storeInConstantPool(index, ConstantPoolTag.UTF8, numConstantPoolStrings);
		
		++ numConstantPoolStrings;
	}

	@Override
	public final void onConstantPoolMethodHandle(int index, int referenceKind, int referenceIndex) {
		storeInConstantPool(index, ConstantPoolTag.METHOD_HANDLE, referenceKind << 32 | referenceIndex);
	}

	@Override
	public final void onConstantPoolMethodType(int index, int descriptorIndex) {
		storeInConstantPool(index, ConstantPoolTag.METHOD_TYPE, descriptorIndex);
	}

	@Override
	public final void onConstantPoolInvokeDynamic(int index, int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
		storeInConstantPool(index, ConstantPoolTag.INVOKE_DYNAMIC, bootstrapMethodAttrIndex << 32 | nameAndTypeIndex);
	}

	@Override
	public final void onClassInfo(int accessFlags, int thisClass, int superClass) {

		this.accessFlags = accessFlags;
		this.thisClass = thisClass;
		this.superClass = superClass;
	}

	
	@Override
	public final void onInterfacesCount(int count) {
		interfaces = new int[count];
	}

	@Override
	public final void onInterface(int index, int nameIndex) {
		interfaces[index] = nameIndex;
	}

	@Override
	public final void onFieldCount(int count) {
		fields = new Field[count];
	}

	@Override
	public final void onField(int index, int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {
		fields[index] = new Field(accessFlags, nameIndex, descriptorIndex, attributesCount);
	}

	@Override
	public final void onMethodCount(int count) {
		methods = new Method[count];
	}

	@Override
	public final void onMethod(int index, int accessFlags, int nameIndex, int descriptorIndex, int attributesCount) {
		methods[index] = new Method(accessFlags, nameIndex, descriptorIndex, attributesCount);
	}

	@Override
	public final void onClassFileAttributeCount(int count) {
		
	}
	
	private final ClassFileAttributesListener methodAttributeListener = new ClassFileAttributesListener() {
		
		@Override
		public void onStackMapTable(int memberIndex, int attributeLength, DataInput dataInput) {
			
		}
		
		@Override
		public void onExceptions(int memberIndex, int attributeLength, DataInput dataInput) {
			
		}
		
		@Override
		public void onConstantValue(int memberIndex, int constantValueIndex) {
			
		}
		
		@Override
		public void onCode(int memberIndex, int attributeLength, DataInput dataInput) {
			try {
				final int maxStack = dataInput.readUnsignedShort();
				final int maxLocals = dataInput.readUnsignedShort();
				
				final int codeLength = dataInput.readInt();
				
				final byte [] data = new byte[codeLength];

				dataInput.readFully(data);

				methods[memberIndex].setBytecode(maxLocals, maxStack, data);
			}
			catch (IOException ex) {
				throw new IllegalStateException(ex);
			}
		}
	};
	
	
	@Override
	public final void onMethodAttribute(
			int methodIndex,
			int attributeIndex,
			int attributeNameIndex,
			int attributeLength,
			DataInput dataInput) throws IOException {

		ClassFileReader.parseAttribute(getUTF8(attributeNameIndex), methodIndex, attributeLength, dataInput, methodAttributeListener);
	}
}
