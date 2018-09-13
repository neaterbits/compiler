package com.neaterbits.compiler.common.parser;

import java.util.Collection;
import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationCodeLines;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;

public abstract class BaseParserListener {
	
	private static class CodeStackEntry extends ListStackEntry<CompilationCode> {
		
		private final String name;

		public CodeStackEntry() {
			this.name = null;
		}
		
		public CodeStackEntry(String name) {
			this.name = name;
		}
	}

	// Stack for the main elements of a program
	private final ListStack<CompilationCode, CodeStackEntry> mainStack;
	
	protected BaseParserListener() {
		this.mainStack = new ListStack<>();
	}
	
	public final void onCompilationUnitStart() {
		
		if (!mainStack.isEmpty()) {
			throw new IllegalStateException("Expected empty stack");
		}
		
		mainStack.push(new CodeStackEntry());
	}
	
	public CompilationUnit onCompilationUnitEnd(Context context) {
		return new CompilationUnit(context, mainStack.pop().getList());
	}

	public final void onNamespaceStart() {
		mainStack.push(new CodeStackEntry());
	}
	
	public final void onNameSpaceEnd(Context context, String name, String [] parts) {
		final List<CompilationCode> namespaceCode = mainStack.pop().getList();

		final Namespace nameSpace = new Namespace(context, name, parts, new CompilationCodeLines(context, namespaceCode));
		
		mainStack.addElement(nameSpace);
	}
	
	public final void onClassStart(String name) {
		mainStack.push(new CodeStackEntry(name));
	}
	
	public final void onClassEnd(Context context) {
		
		final CodeStackEntry entry = mainStack.pop();
		
		final List<CompilationCode> classCode = entry.getList();

		final ClassDefinition classDefinition = new ClassDefinition(context, new ClassName(entry.name), cast(classCode));
		
		mainStack.addElement(classDefinition);
	}
	
	@SuppressWarnings("unchecked")
	private static <S, T extends S, C extends Collection<S>> List<T> cast(C collection) {
		return (List<T>)collection;
	}
}
