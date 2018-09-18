package com.neaterbits.compiler.common.log;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.util.Strings;

public final class ParseLogger {
	
	private String lastAntlrContent;
	private List<String> antlrRules;
	
	private boolean lastIsEnter;
	
	private int stackLevel;
	
	private final PrintStream out;

	public ParseLogger(PrintStream out) {

		Objects.requireNonNull(out);
		
		this.out = out;

		this.antlrRules = new ArrayList<>();
	}
	
	public void onEnterAntlrRule(String name, String content) {
	
		Objects.requireNonNull(name);
		Objects.requireNonNull(content);
		
		if (lastAntlrContent == null) {
			this.lastAntlrContent = content;
		}
		else if (!lastIsEnter) {
			printRulesAndClear(false, content);
		}
		else if (!content.equals(lastAntlrContent)) {
			printRulesAndClear(true, content);
		}
		
		antlrRules.add(name);
		
		this.lastIsEnter = true;
	}
	
	public void onExitAntlrRule(String name, String content) {

		Objects.requireNonNull(name);
		Objects.requireNonNull(content);
		
		if (lastAntlrContent == null) {
			throw new IllegalStateException("Starting with exit-rule");
		}
		else if (lastIsEnter) {
			printRulesAndClear(true, content);
		}
		else if(!content.equals(lastAntlrContent)) {
			printRulesAndClear(false, content);
		}
		
		antlrRules.add(name);
		
		this.lastIsEnter = false;
	}
	
	private PrintStream indent() {

		for (int i = 0; i < stackLevel; ++ i) {
			out.append("  ");
		}

		return out;
	}
	
	public void onStackPush(String type) {

		out.append("--");
		indent().append("push ").append(type).println();
	
		++ stackLevel;
	}

	public void onStackAddElement(String type) {

		out.append("--");
		indent().append("addToElement ").append(type).println();
	}

	public void onStackSetElement(String s) {

		out.append("--");
		indent().append("setElement ").append(s).println();
	}

	public void onStackPop(String type) {

		-- stackLevel;

		out.append("--");
		indent().append("pop ").append(type).println();
	}
	
	private void printRulesAndClear(boolean enter, String newContent) {
		printRules(enter, lastAntlrContent, antlrRules);

		antlrRules.clear();
		
		this.lastAntlrContent = newContent;
	}

	private void printRules(boolean enter, String content, List<String> ruleNames) {
		
		out.append('[').append(enter ? "enter" : "exit").append("] ")
			.append(content.length() > 30 ? content.substring(0, 30) : content)
			
			.append(" # ");
		
		Strings.outputList(ruleNames, enter ? " => " : " <= ", s -> s, out::append);
		
		out.println();
	}
}
