package dev.nimbler.compiler.language.java.parser.antlr;

import java.util.Objects;

import dev.nimbler.compiler.util.Base;

final class JavaParserUtil {

	static JavaInteger parseIntegerLiteral(String literal) {
		
		Objects.requireNonNull(literal);

		if (!literal.equals(literal.trim())) {
			throw new IllegalArgumentException("String not trimmed");
		}

		final long value;
		final Base base;
		final int bits;
		
		if (literal.endsWith("l") || literal.endsWith("L")) {
			literal = literal.substring(0, literal.length() - 1);
			bits = 64;
		}
		else {
			bits = 32;
		}

		if (literal.equals("0")) {
			value = 0L;
			base = Base.DECIMAL;
		}
		else if (literal.startsWith("0b") || literal.startsWith("0B")) {
			value = parseLong(literal.substring(2), 2);
			base = Base.BINARY;
		}
		else if (literal.startsWith("0x") || literal.startsWith("0X")) {
			value = parseLong(literal.substring(2), 16);
			base = Base.HEX;
		}
		else if (literal.startsWith("0") || literal.startsWith("0")) {
			value = parseLong(literal.substring(1), 8);
			base = Base.OCTAL;
		}
		else {
			value = parseLong(literal, 10);
			base = Base.DECIMAL;
		}

		return new JavaInteger(value, base, bits);
	}
	
	private static long parseLong(String s, int radix) {
		
		if (radix > 16) {
			throw new IllegalArgumentException("radix > 16");
		}
		
		long number = 0;
		
		final boolean negative;
		
		if (s.charAt(0) == '-') {
			negative = true;
			s = s.substring(1);
		}
		else {
			negative = false;
		}
		
		
		for (int i = 0; i < s.length(); ++ i) {
			final char c = s.charAt(i);
			
			if (c == '_') {
				continue;
			}
			
			final int digit;
			
			if (Character.isDigit(c)) {
				digit = c - '0';
			}
			else if (c >= 'A' && c <= 'F') {
				digit = 10 + (c - 'A');
			}
			else if (c >= 'a' && c <= 'f') {
				digit = 10 + (c - 'a');
			}
			else {
				throw new IllegalArgumentException("Character out of range: " + c);
			}
			
			if (digit > radix) {
				throw new IllegalArgumentException("digit > radix: " + c);
			}
			
			number *= radix;
			
			number += digit;
		}

		return negative ? -number : number;
	}
}
