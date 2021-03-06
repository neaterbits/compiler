package dev.nimbler.language.bytecode.java;

public class AccessFlags {
	public static final int ACC_PUBLIC 		= 0x0001;
	public static final int ACC_PRIVATE		= 0x0002;
	public static final int ACC_PROTECTED	= 0x0004;
	public static final int ACC_STATIC		= 0x0008;
	public static final int ACC_FINAL 		= 0x0010;
	public static final int ACC_SUPER 		= 0x0020;
	public static final int ACC_VOLATILE	= 0x0040;
	public static final int ACC_TRANSIENT	= 0x0080;
	public static final int ACC_INTERFACE 	= 0x0200;
	public static final int ACC_ABSTRACT 	= 0x0400;
	public static final int ACC_ANNOTATION 	= 0x1000;
	public static final int ACC_SYNTHETIC 	= 0x2000;
	public static final int ACC_ENUM 		= 0x4000;
}
