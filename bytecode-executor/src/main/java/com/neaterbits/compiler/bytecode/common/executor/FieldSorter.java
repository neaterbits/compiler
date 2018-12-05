package com.neaterbits.compiler.bytecode.common.executor;

import com.neaterbits.compiler.bytecode.common.ClassFields;
import com.neaterbits.compiler.common.FieldType;

class FieldSorter {

	@FunctionalInterface
	interface IntPredicate {
		boolean test(int index);
	}

	static long [] sortFieldsForType(ClassFields classFields, int referenceSizeInBytes, IntPredicate filter) {
		
		int num1bit = 0;
		int num8bit = 0;
		int num16bit = 0;
		int num32bit = 0;
		int num64bit = 0;
		int numReferences = 0;
		int numArrays = 0;
		
		int fieldCount = 0;
		
		for (int i = 0; i < classFields.getFieldCount(); ++ i) {
			if (filter.test(i)) {
				final FieldType fieldType = classFields.getFieldType(i);
			
				switch (fieldType.getSizeInBits()) {
				case 1: ++ num1bit;	break;
				case 8: ++ num8bit; break;
				case 16: ++ num16bit; break;
				case 32: ++ num32bit; break;
				case 64: ++ num64bit; break;
				case -1:
		
					if (fieldType.getArrayDimensions() > 0) {
						++ numArrays;
					}
					else {
						++ numReferences;
					}
					break;
					
				default:
					throw new UnsupportedOperationException();
				}
				
				++ fieldCount;
			}
		}
		
		int num8bitPad = 0;
		int num16bitPad = 0;
		int num32bitpad = 0;
		
		final int num1bitBytes = ((num1bit + 1) / 8) - 1;
		
		final int num8bitBytes = num1bitBytes + num8bit;
		
		if (num16bit > 0) {
			num8bitPad = pad(num8bitBytes, 1, 2);
		}
		else if (num32bit > 0) {
			num8bitPad = pad(num8bitBytes, 1, 4);
		}
		else if (num64bit > 0) {
			num8bitPad = pad(num8bitBytes, 1, 8);
		}
		else if (numReferences > 0 || numArrays > 0) {
			num8bitPad = pad(num8bitBytes, 1, referenceSizeInBytes);
		}

		if (num16bit > 0) {
			if (num32bit > 0) {
				num16bitPad = pad(num16bit, 2, 4);
			}
			else if (num64bit > 0) {
				num16bitPad = pad(num16bit, 2, 8);
			}
			else if (numReferences > 0 || numArrays > 0) {
				num16bitPad = pad(num16bit, 2, referenceSizeInBytes);
			}
		}

		if (num32bit > 0) {
			if (num64bit > 0) {
				num32bitpad = pad(num32bit, 4, 8);
			}
			else if (numReferences > 0 || numArrays > 0) {
				num32bitpad = pad(num32bit, 4, referenceSizeInBytes);
			}
		}

		final long [] fields = new long[fieldCount];

		int byteOffset = 0;
		
		byteOffset += num1bitBytes;
		
		final int baseOffset8bit = byteOffset;
		byteOffset += num8bitBytes + num8bitPad;
		
		final int baseOffset16bit = byteOffset;
		byteOffset += (num16bit + num16bitPad) * 2;
		
		final int baseOffset32bit = byteOffset;
		byteOffset += (num32bit + num32bitpad) * 4;

		final int baseOffset64bit = byteOffset;
		byteOffset += num64bit * 8;
		
		final int baseOffsetReference = byteOffset;
		
		int idx1bit = 0;
		int idx8bit = 0;
		int idx16bit = 0;
		int idx32bit = 0;
		int idx64bit = 0;
		int idxReferences = 0;
		
		for (int i = 0; i < classFields.getFieldCount(); ++ i) {
			if (filter.test(i)) {
				final FieldType fieldType = classFields.getFieldType(i);
				
				final long field;
			
				switch (fieldType.getSizeInBits()) {
				case 1:
					final int byteIndex = idx1bit / 8;
					final int bitIndex = idx1bit % 8;
					
					field = field(i, byteIndex, bitIndex, true);
					break;
					
				case 8:
					field = field(i, baseOffset8bit, 8, idx8bit ++);
					break;
					
				case 16:
					field = field(i, baseOffset16bit, 16, idx16bit ++);
					break;
					
				case 32:
					field = field(i, baseOffset32bit, 32, idx32bit ++);
					break;

				case 64:
					field = field(i, baseOffset64bit, 64, idx64bit ++);
					break;
					
				case -1:
					field = field(i, baseOffsetReference, referenceSizeInBytes, idxReferences ++);
					break;
					
				default:
					throw new UnsupportedOperationException();
				}
			
				fields[i] = field;
			}
		}

		return fields;
	}
	
	static int getFieldsMemorySize(long [] sortedFields) {
		final long lastField = sortedFields[sortedFields.length - 1];
		
		final int fieldOffset = fieldOffset(lastField);
		final int fieldSize = fieldSize(lastField);
		
		final int size = fieldOffset + fieldSize;

		return size;
	}
	
	private static int pad(int num, int sizeInBytes, int alignment) {
		throw new UnsupportedOperationException("TODO");
	}

	private static long field(int fieldIdx, int baseOffset, int fieldSize, boolean bitBased) {
		long field = fieldIdx << 48 | baseOffset << 16 | fieldSize << 8;
		
		if (bitBased) {
			field |= 1L << 63;
		}
		
		return field;
	}

	private static long field(int fieldIdx, int offset, int size, int index) {
		return field(fieldIdx, offset + size * index, size << 8, false);
	}
	
	private static int fieldOffset(long field) {
		return (int)((field >> 16) & 0xFFFF);
	}
	
	private static int fieldSize(long field) {
		return (int)((field >> 8) & 0xFF);
	}
}
