package com.neaterbits.compiler.emit;

import java.math.BigInteger;
import java.util.Objects;

import com.neaterbits.compiler.ast.expression.Base;

public class EmitterState {

	private final char newline;
	private final StringBuilder sb;

	private int indent;
	private boolean atStartOfLine;
	
	public EmitterState(char newline) {
		this.newline = newline;
		this.sb = new StringBuilder();
		
		this.indent = 0;
		this.atStartOfLine = true;
	}

	private void indentIfAtStartOfLine() {
		
		if (atStartOfLine) {
			for (int i = 0; i < indent; ++ i) {
				sb.append("  ");
			}
			
			this.atStartOfLine = false;
		}
	}
	
	public final EmitterState append(BigInteger bigInt, Base base) {

		append(bigInt.toString(base.getRadix()));

		return this;
	}
	
	public final EmitterState append(String s) {
		
		Objects.requireNonNull(s);
		
		indentIfAtStartOfLine();
		
		sb.append(s);
		
		return this;
	}

	public final EmitterState append(char c) {
		indentIfAtStartOfLine();

		sb.append(c);
		
		return this;
	}
	
	public final EmitterState newline() {
		sb.append(newline);
		
		this.atStartOfLine = true;
		
		return this;
	}
	
	public final EmitterState addIndent() {
		++ indent;
		
		return this;
	}
	
	public final EmitterState subIndent() {
		if (indent == 0) {
			throw new IllegalStateException("indent == 0");
		}

		-- indent;

		return this;
	}

	public final String asString() {
		return sb.toString();
	}
}
