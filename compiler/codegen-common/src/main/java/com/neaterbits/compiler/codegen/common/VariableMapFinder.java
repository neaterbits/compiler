package com.neaterbits.compiler.codegen.common;

import com.neaterbits.compiler.bytecode.common.BytecodeInstructions;
import com.neaterbits.compiler.bytecode.common.BytecodeSwitchCase;
import com.neaterbits.compiler.bytecode.common.ClassBytecode;

public class VariableMapFinder {

	public static void findVariableMap(
			ClassBytecode classBytecode,
			int methodIdx,
			BytecodeSwitchCase bytecodeSwitchCase,
			VariableMap dst) {
		
		final byte [] bytecode = classBytecode.getMethodBytecode(methodIdx);
		
		final BytecodeListener bytecodeListener = new BytecodeListener(classBytecode, methodIdx, dst);
		
		for (int i = 0; i < bytecode.length;) {
			
			i += bytecodeSwitchCase.switchCase(bytecode, i, bytecodeListener);
			
		}
	}
	
	private static final class BytecodeListener implements BytecodeInstructions {
		
		
		private static final long FLAG_CONSTANT = 1L << 0;
		private static final long FLAG_NULL = 1L << 1;
		
		
		private static final int FLAG_BITS = 2;
		private static final int INSTRUCTION_OFFSET_BITS = 22;
		private static final int VARIABLENO_BITS = 20;
		private static final long VARIABLENO_MASK = (1L << (INSTRUCTION_OFFSET_BITS + FLAG_BITS)) - 1;
		// private static final int TYPE_BITS = IdentifierBits.TYPE_BITS;
		
		private final VariableMap dst;
		
		// Encoded on the stack with upper 20 bits being type,
		// 20 bits being variableNo
		// lower 24 being scope start offset
		private final long [] stack;
		private int curStackSize;
		
		private final int [] localVariableTypes;
		
		private int variableNo;

		BytecodeListener(ClassBytecode classBytecode, int methodIdx, VariableMap dst) {
			
			final int maxLocals = classBytecode.getMethodMaxLocals(methodIdx);
			
			this.localVariableTypes = new int[maxLocals];
			
			this.dst = dst;
			
			final int stackSize = classBytecode.getMethodMaxOperandStack(methodIdx) + maxLocals;
			
			this.stack = new long[stackSize];
			this.curStackSize = 0;
		}

		private void pushNull(int instructionOffset) {
			push(instructionOffset, 0, FLAG_NULL|FLAG_CONSTANT);
		}

		private void push(int instructionOffset, int type, long flags) {
			final long value =
					  type << (VARIABLENO_BITS + INSTRUCTION_OFFSET_BITS + FLAG_BITS)
					| variableNo ++ << (INSTRUCTION_OFFSET_BITS + FLAG_BITS)
					| instructionOffset << FLAG_BITS
					| flags;
		
			stack[curStackSize ++] = value;
		}

		
		private void push(int instructionOffset, int type) {
			push(instructionOffset, type, 0L);
		}
		
		private int pop() {
			-- curStackSize;
			
			final long value = stack[curStackSize];
			
			return (int)((value & VARIABLENO_MASK) >> (INSTRUCTION_OFFSET_BITS + FLAG_BITS));
		}

		@Override
		public void incrementVariableBySBYTE(int instructionOffset, int index, byte value) {
			
		}

		@Override
		public void loadReferenceFromArray(int instructionOffset) {
			
			final int indexVariable = pop();
			final int arrayRefVariable = pop();
			
			dst.startVariable(instructionOffset, arrayRefVariable, indexVariable);
		}


		@Override
		public void storeReferenceInArray(int instructionOffset) {

			final int valueVariable = pop();
			final int indexVariable = pop();
			final int arrayRefVariable = pop();
			
			dst.startVariable(instructionOffset, arrayRefVariable, indexVariable, valueVariable);
		}


		@Override
		public void pushNullReference(int instructionOffset) {

			pushNull(instructionOffset);
			
		}


		@Override
		public void pushReferenceFromVariable(int instructionOffset, int index) {
			push(instructionOffset, localVariableTypes[index]);
		}


		@Override
		public void createArray(int instructionOffset, int type) {

			final int count = pop();
			
			push(instructionOffset, dst.startVariable(instructionOffset, count));
		}


		@Override
		public void returnReference(int instructionOffset) {

			final int returnVariable = pop();
			
			dst.endVariable(instructionOffset, returnVariable);
		}


		@Override
		public void arrayLength(int instructionOffset) {

			final int arrayRefVariable = pop();
			
			final int lengthVariable = dst.startVariable(instructionOffset, arrayRefVariable);
			
			push(instructionOffset, lengthVariable);
		}


		@Override
		public void storeReferenceInVariable(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void throwException(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void loadByteOrBooleanFromArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void storeByteOrBooleanInArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushByteAsInteger(int instructionOffset, byte value) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void loadCharFromArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void storeCharInArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void checkCast(int instructionOffset, int type) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertDoubleToFloat(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertDoubleToInt(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertDoubleToLong(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void addTwoDoubles(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void loadDoubleFromArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void storeDoubleInArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void compareTwoDoubles(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushDoubleConstant0(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushDoubleConstant1(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void divideTwoDoubles(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void loadDoubleFromVariable(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void multiplyTwoDoubles(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void negateDouble(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void getDoubleRemainder(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void returnDouble(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void storeDoubleInVariable(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void subtractDouble(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void duplicateValue(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertFloatToDouble(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertFloatToInt(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertFloatToLong(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void addTwoFloats(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void loadFloatFromArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void storeFloatInArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void compareTwoFloats(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushFloatConstant0(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushFloatConstant1(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushFloatConstant2(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void divideTwoFloats(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void loadFloatFromVariable(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void multiplyTwoFloats(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void negateFloat(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void getFloatRemainder(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void returnFloat(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void storeFloatInVariable(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void subtractFloat(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void getField(int instructionOffset, int field) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void getStatic(int instructionOffset, int type, int field) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void gotoBranchOffset(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertIntToByte(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertIntToCharacter(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertIntToDouble(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertIntToFloat(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertIntToLong(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertIntToShort(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void addTwoInts(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void loadIntFromArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void bitwiseAndTwoInts(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void storeIntInArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void compareTwoInts(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushIntConstantMinus1(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushIntConstant0(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushIntConstant1(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushIntConstant2(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushIntConstant3(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushIntConstant4(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushIntConstant5(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifReferencesAreEqual(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifReferencesAreNotEqual(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifIntsAreEqual(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifGreaterThanOrEqual(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifGreaterThan(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifLessThanOrEqual(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifLessThan(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifIntsAreNotEqual(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifValueIs0(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifGreaterThanOrEqual0(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifGreaterThan0(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifLessThanOrEqual0(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifLessThan0(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifNot0(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifNotNull(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void ifNull(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void divideTwoInts(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void loadIntFromVariable(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void multiplyTwoInts(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void negateInt(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void getIntRemainder(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void instanceOf(int instructionOffset, int type) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void invokeDynamic(int instructionOffset, int reference) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void invokeInterface(int instructionOffset, int reference) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void invokeSpecial(int instructionOffset, int reference) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void invokeStatic(int instructionOffset, int reference) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void invokeVirtual(int instructionOffset, int reference) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void bitwiseOrTwoInts(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void logicalIntRemainder(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void returnInt(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void shiftLeftInt(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void arithmeticShiftRightInt(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void storeIntInVariable(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void subtractInt(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void logicalShiftRightInt(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void exlusiveOrInt(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void jumpSubroutine(int instructionOffset, int branchOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertLongToDouble(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertLongToFloat(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void convertLongToInt(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void addTwoLongs(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void loadLongFromArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void bitwiseAndTwoLongs(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void storeLongInArray(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void longCmp(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushLongConstant0(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushLongConstant1(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushConstant32(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pushConstant64(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void divideTwoLongs(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void loadLongFromVariable(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void multiplyTwoLongs(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void negateLong(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void bitwiseOrTwoLongs(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void getLongRemainder(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void returnLongValue(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void bitwiseShiftLeftLong(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void bitwiseShiftRightLong(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void storeLongInVariable(int instructionOffset, int index) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void subtractTwoLongs(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void bitwiseShiftRightLongUnsigned(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void bitwiseExclusiveOrLong(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void monitorEnter(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void monitorExit(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void createMultiDimensionalArray(int instructionOffset, int type, int numDimensions) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void createObject(int instructionOffset, int type) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void createPrimitiveTypeArray(int instructionOffset, int type) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pop32(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void pop64(int instructionOffset) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void putField(int instructionOffset, int field) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void putStatic(int instructionOffset, int type, int field) {
			// TODO Auto-generated method stub
			
		}
	}
}
