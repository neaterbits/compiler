package com.neaterbits.compiler.codegen.x86;

import com.neaterbits.compiler.bytecode.common.BytecodeInstructions;
import com.neaterbits.compiler.codegen.common.AllocatedStackAndRegisters;
import com.neaterbits.compiler.codegen.common.BytecodeToMachinecode;
import com.neaterbits.compiler.codegen.common.OperandType;

import static com.neaterbits.compiler.codegen.common.OperandEncoding.*;

public final class BytecodeToX86StackBased extends BytecodeToMachinecode implements BytecodeInstructions {

	private AllocatedStackAndRegisters allocatedStackAndRegisters;
	
	private static final boolean isREXRegister(int offset) {
		return offset > 7;
	}
	
	private static byte offsetToRegisterCode(int offset) {
		return (byte)(offset > 7 ? offset - 8 : offset);
	}

	private static byte rexOffsetToRegisterCode(int offset) {
		
		if (offset < 8 || offset > 15) {
			throw new IllegalArgumentException();
		}
		
		return (byte)(offset - 8);
	}

	@Override
	public void incrementVariableBySBYTE(int instructionOffset, int localVariableIndex, byte value) {
		
		final long srcOperand = allocatedStackAndRegisters.getSrcOperandEncoded(instructionOffset, 0);
		final long dstOperand = allocatedStackAndRegisters.getDstOperandEncoded(instructionOffset, 0);
		
		if (srcOperand != dstOperand) {
			throw new IllegalStateException();
		}
		
		final int length;

		final int offset = getOffset(srcOperand); 

		if (isRegisterOperand(srcOperand)) {
			
			final int operandType = getType(srcOperand);
			
			if (value == 1) {
				
				if (isREXRegister(offset)) {
					length = X86Instructions.INC64(buffer, count, (byte)(REX.ENABLE|REX.W|REX.B), rexOffsetToRegisterCode(offset));
				}
				else {
				
					switch (operandType) {
					case OperandType.SBYTE:
					case OperandType.UBYTE:
						length = X86Instructions.INC8(buffer, count, (byte)offset);
						break;
						
					case OperandType.SWORD:
					case OperandType.UWORD:
						length = X86Instructions.INC16(buffer, count, (byte)offset);
						break;
						
					case OperandType.SDWORD:
					case OperandType.UDWORD:
						length = X86Instructions.INC32(buffer, count, (byte)offset);
						break;
						
					case OperandType.SQWORD:
					case OperandType.UQWORD:
						length = X86Instructions.INC64(buffer, count, (byte)(REX.ENABLE|REX.W), (byte)offset);
						break;
					
					default:
						throw new IllegalStateException();
					}
				}
			}
			else if (value == -1) {
				if (isREXRegister(offset)) {
					length = X86Instructions.DEC64(buffer, count, (byte)(REX.ENABLE|REX.W|REX.B), rexOffsetToRegisterCode(offset));
				}
				else {
				
					switch (operandType) {
					case OperandType.SBYTE:
					case OperandType.UBYTE:
						length = X86Instructions.DEC8(buffer, count, (byte)offset);
						break;
						
					case OperandType.SWORD:
					case OperandType.UWORD:
						length = X86Instructions.DEC16(buffer, count, (byte)offset);
						break;
						
					case OperandType.SDWORD:
					case OperandType.UDWORD:
						length = X86Instructions.DEC32(buffer, count, (byte)offset);
						break;
						
					case OperandType.SQWORD:
					case OperandType.UQWORD:
						length = X86Instructions.INC64(buffer, count, (byte)(REX.ENABLE|REX.W), (byte)offset);
						break;
					
					default:
						throw new IllegalStateException();
					}
				}
				
				
			}
			else {
				if (isREXRegister(offset)) {
					length = X86Instructions.ADD_SIGNED_IMM8_TO_REG64(buffer, count, (byte)(REX.ENABLE|REX.W|REX.B), rexOffsetToRegisterCode(offset), value);
				}
				else {
				
					switch (operandType) {
					case OperandType.SBYTE:
						length = X86Instructions.ADD_SIGNED_IMM8_TO_REG8(buffer, count, (byte)offset, value);
						break;
						
					case OperandType.SWORD:
						length = X86Instructions.ADD_SIGNED_IMM8_TO_REG16(buffer, count, (byte)offset, value);
						break;
						
					case OperandType.SDWORD:
						length = X86Instructions.ADD_SIGNED_IMM8_TO_REG32(buffer, count, (byte)offset, value);
						break;
						
					case OperandType.SQWORD:
						length = X86Instructions.ADD_SIGNED_IMM8_TO_REG64(buffer, count, (byte)(REX.ENABLE|REX.W), (byte)offset, value);
						break;
					
					default:
						throw new IllegalStateException();
					}
				}
			}
			
		}
		else {
			// On stack
			
			/*
			final int memRegister = allocatedStackAndRegisters.getMemIndirectScratchRegister();
			
			byte rex;
			
			int register;
			
			int addLength;
			
			if (isREXRegister(memRegister)) {
				rex = (byte)(REX.ENABLE|REX.W|REX.B);
				register = rexOffsetToRegisterCode(memRegister);
				
				addLength = X86Instructions.LEA64_16BIT_OFFSET(buffer, count, register, offset);
			}
			else {
				rex = (byte)(REX.ENABLE|REX.W);
				register = memRegister;
			}
			
			int addLength = X86Instructions.LEA64_16BIT_OFFSET(buffer, count, register, offset);
			
			
			switch (operandType) {
			case OperandType.SBYTE:
				length = X86Instructions.ADD_SIGNED_IMM8_TO_REG8(buffer, count, (byte)offset, value);
				break;
				
			case OperandType.SWORD:
				length = X86Instructions.ADD_SIGNED_IMM8_TO_REG16(buffer, count, (byte)offset, value);
				break;
				
			case OperandType.SDWORD:
				length = X86Instructions.ADD_SIGNED_IMM8_TO_REG32(buffer, count, (byte)offset, value);
				break;
				
			case OperandType.SQWORD:
				length = X86Instructions.ADD_SIGNED_IMM8_TO_REG64(buffer, count, (byte)(REX.ENABLE|REX.W), (byte)offset, value);
				break;
			
			default:
				throw new IllegalStateException();
			}
			*/
			throw new UnsupportedOperationException("TODO");
		}
	}

	@Override
	public void loadReferenceFromArray(int instructionOffset) {

		
		
		
	}

	@Override
	public void storeReferenceInArray(int instructionOffset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushNullReference(int instructionOffset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushReferenceFromVariable(int instructionOffset, int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createArray(int instructionOffset, int type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnReference(int instructionOffset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void arrayLength(int instructionOffset) {
		// TODO Auto-generated method stub
		
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
