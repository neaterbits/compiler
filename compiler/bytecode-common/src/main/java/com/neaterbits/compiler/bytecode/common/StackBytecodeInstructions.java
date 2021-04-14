package com.neaterbits.compiler.bytecode.common;

public interface StackBytecodeInstructions {

	void loadReferenceFromArray(int instructionOffset);
	void storeReferenceInArray(int instructionOffset);
	void pushNullReference(int instructionOffset);
	
	void pushReferenceFromVariable(int instructionOffset, int index);
	
	void createArray(int instructionOffset, int type);
	
	void returnReference(int instructionOffset);
	
	void arrayLength(int instructionOffset);
	
	void storeReferenceInVariable(int instructionOffset, int index);
	
	void throwException(int instructionOffset);
	
	void loadByteOrBooleanFromArray(int instructionOffset);
	void storeByteOrBooleanInArray(int instructionOffset);
	
	void pushByteAsInteger(int instructionOffset, byte value);
	
	void loadCharFromArray(int instructionOffset);
	void storeCharInArray(int instructionOffset);
	
	void checkCast(int instructionOffset, int type);
	
	void convertDoubleToFloat(int instructionOffset);
	void convertDoubleToInt(int instructionOffset);
	void convertDoubleToLong(int instructionOffset);
	
	void addTwoDoubles(int instructionOffset);
	void loadDoubleFromArray(int instructionOffset);
	void storeDoubleInArray(int instructionOffset);
	void compareTwoDoubles(int instructionOffset);
	void pushDoubleConstant0(int instructionOffset);
	void pushDoubleConstant1(int instructionOffset);
	void divideTwoDoubles(int instructionOffset);
	void loadDoubleFromVariable(int instructionOffset, int index);
	void multiplyTwoDoubles(int instructionOffset);
	void negateDouble(int instructionOffset);
	void getDoubleRemainder(int instructionOffset);
	void returnDouble(int instructionOffset);
	void storeDoubleInVariable(int instructionOffset, int index);
	void subtractDouble(int instructionOffset);
	
	void duplicateValue(int instructionOffset);
	
	void convertFloatToDouble(int instructionOffset);
	void convertFloatToInt(int instructionOffset);
	void convertFloatToLong(int instructionOffset);

	void addTwoFloats(int instructionOffset);
	void loadFloatFromArray(int instructionOffset);
	void storeFloatInArray(int instructionOffset);
	void compareTwoFloats(int instructionOffset);
	void pushFloatConstant0(int instructionOffset);
	void pushFloatConstant1(int instructionOffset);
	void pushFloatConstant2(int instructionOffset);
	void divideTwoFloats(int instructionOffset);
	void loadFloatFromVariable(int instructionOffset, int index);
	void multiplyTwoFloats(int instructionOffset);
	void negateFloat(int instructionOffset);
	void getFloatRemainder(int instructionOffset);
	void returnFloat(int instructionOffset);
	void storeFloatInVariable(int instructionOffset, int index);
	void subtractFloat(int instructionOffset);

	void getField(int instructionOffset, int field);
	void getStatic(int instructionOffset, int type, int field);
	
	void gotoBranchOffset(int instructionOffset, int branchOffset);

	void convertIntToByte(int instructionOffset);
	void convertIntToCharacter(int instructionOffset);
	void convertIntToDouble(int instructionOffset);
	void convertIntToFloat(int instructionOffset);
	void convertIntToLong(int instructionOffset);
	void convertIntToShort(int instructionOffset);

	void addTwoInts(int instructionOffset);
	void loadIntFromArray(int instructionOffset);
	void bitwiseAndTwoInts(int instructionOffset);
	void storeIntInArray(int instructionOffset);
	void compareTwoInts(int instructionOffset);
	void pushIntConstantMinus1(int instructionOffset);
	void pushIntConstant0(int instructionOffset);
	void pushIntConstant1(int instructionOffset);
	void pushIntConstant2(int instructionOffset);
	void pushIntConstant3(int instructionOffset);
	void pushIntConstant4(int instructionOffset);
	void pushIntConstant5(int instructionOffset);
	
	void ifReferencesAreEqual(int instructionOffset, int branchOffset);
	void ifReferencesAreNotEqual(int instructionOffset, int branchOffset);
	
	void ifIntsAreEqual(int instructionOffset, int branchOffset);
	void ifGreaterThanOrEqual(int instructionOffset, int branchOffset);
	void ifGreaterThan(int instructionOffset, int branchOffset);
	void ifLessThanOrEqual(int instructionOffset, int branchOffset);
	void ifLessThan(int instructionOffset, int branchOffset);
	void ifIntsAreNotEqual(int instructionOffset, int branchOffset);
	void ifValueIs0(int instructionOffset, int branchOffset);
	void ifGreaterThanOrEqual0(int instructionOffset, int branchOffset);
	void ifGreaterThan0(int instructionOffset, int branchOffset);
	void ifLessThanOrEqual0(int instructionOffset, int branchOffset);
	void ifLessThan0(int instructionOffset, int branchOffset);
	void ifNot0(int instructionOffset, int branchOffset);
	void ifNotNull(int instructionOffset, int branchOffset);
	void ifNull(int instructionOffset, int branchOffset);
	
	
	void divideTwoInts(int instructionOffset);
	void loadIntFromVariable(int instructionOffset, int index);
	void multiplyTwoInts(int instructionOffset);
	void negateInt(int instructionOffset);
	void getIntRemainder(int instructionOffset);

	void instanceOf(int instructionOffset, int type);
	
	void invokeDynamic(int instructionOffset, int reference);
	void invokeInterface(int instructionOffset, int reference);
	void invokeSpecial(int instructionOffset, int reference);
	void invokeStatic(int instructionOffset, int reference);
	void invokeVirtual(int instructionOffset, int reference);

	void bitwiseOrTwoInts(int instructionOffset);
	void logicalIntRemainder(int instructionOffset);
	void returnInt(int instructionOffset);
	
	void shiftLeftInt(int instructionOffset);
	void arithmeticShiftRightInt(int instructionOffset);
	
	void storeIntInVariable(int instructionOffset, int index);
	void subtractInt(int instructionOffset);
	
	void logicalShiftRightInt(int instructionOffset);
	void exlusiveOrInt(int instructionOffset);
	
	void jumpSubroutine(int instructionOffset, int branchOffset);
	
	void convertLongToDouble(int instructionOffset);
	void convertLongToFloat(int instructionOffset);
	void convertLongToInt(int instructionOffset);
	
	void addTwoLongs(int instructionOffset);
	void loadLongFromArray(int instructionOffset);
	void bitwiseAndTwoLongs(int instructionOffset);
	void storeLongInArray(int instructionOffset);
	void longCmp(int instructionOffset);
	
	void pushLongConstant0(int instructionOffset);
	void pushLongConstant1(int instructionOffset);
	
	void pushConstant32(int instructionOffset, int index);
	void pushConstant64(int instructionOffset, int index);
	
	void divideTwoLongs(int instructionOffset);
	void loadLongFromVariable(int instructionOffset, int index);
	void multiplyTwoLongs(int instructionOffset);
	void negateLong(int instructionOffset);
	void bitwiseOrTwoLongs(int instructionOffset);
	void getLongRemainder(int instructionOffset);
	void returnLongValue(int instructionOffset);
	void bitwiseShiftLeftLong(int instructionOffset);
	void bitwiseShiftRightLong(int instructionOffset);
	void storeLongInVariable(int instructionOffset, int index);
	void subtractTwoLongs(int instructionOffset);
	void bitwiseShiftRightLongUnsigned(int instructionOffset);
	void bitwiseExclusiveOrLong(int instructionOffset);

	void monitorEnter(int instructionOffset);
	void monitorExit(int instructionOffset);
	
	void createMultiDimensionalArray(int instructionOffset, int type, int numDimensions);
	void createObject(int instructionOffset, int type);
	void createPrimitiveTypeArray(int instructionOffset, int type);
	
	void pop32(int instructionOffset);
	void pop64(int instructionOffset);
	
	void putField(int instructionOffset, int field);
	void putStatic(int instructionOffset, int type, int field);
}
