package dev.nimbler.compiler.util.parse;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jutils.ArrayStack;
import org.jutils.Strings;
import org.jutils.parse.context.Context;

import dev.nimbler.compiler.util.FullContextProvider;

public final class ParseLogger {
	
	private String lastAntlrContent;
	private List<String> antlrRules;
	
	private boolean lastIsEnter;
	
	private int stackLevel;
	
	private final PrintStream out;
	
	private final FullContextProvider fullContextProvider;
	
	private final ArrayStack<String> loggerStack;

	public ParseLogger(PrintStream out, FullContextProvider fullContextProvider) {

		Objects.requireNonNull(out);
		Objects.requireNonNull(fullContextProvider);
		
		this.out = out;
		this.fullContextProvider = fullContextProvider;

		this.antlrRules = new ArrayList<>();
		
		this.loggerStack = new ArrayStack<>();
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
	
	public void onEnterListenerFunction(String methodName, Context context) {
		final PrintStream stream = indent().append("enter " + methodName);
		
		if (context != null) {
		    
		    final String text = fullContextProvider.getText(context);
		    
		    stream.append(' ').append(text);
		}
		
		stream.println();
	}
	
	public void onExitListenerFunction(String methodName, Context context) {
		final PrintStream stream = indent().append("exit " + methodName);
		
		if (context != null) {

            final String text = fullContextProvider.getText(context);

            stream.append(' ').append(text);
		}
		
		stream.println();
	}

	public void onStackPush(String type, Collection<String> stack) {

		out.append("--");
		indent().append("push ").append(type);
		
		
		// out.append(' ').append(stack.toString());

		appendLoggerStack();

		out.println();
		
		loggerStack.push(type);
	
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

		loggerStack.pop();
		
		out.append("--");
		indent().append("pop ").append(type);

		appendLoggerStack();
		
		out.println();
	}
	
	public void println(String line) {
		out.println(line);
	}
	
	private void appendLoggerStack() {
		out.append(' ').append(loggerStack.stream().collect(Collectors.toList()).toString());
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
