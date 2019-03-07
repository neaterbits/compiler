package com.neaterbits.compiler.common.model;

import java.io.File;
import java.util.Objects;

import com.neaterbits.compiler.common.ArrayStack;
import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.Stack;
import com.neaterbits.compiler.common.StackDelegator;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.Program;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDeclarationName;
import com.neaterbits.compiler.common.parser.ParsedFile;

public final class ObjectProgramModel implements ProgramModel<Program, ParsedFile, CompilationUnit > {

	@Override
	public ParsedFile getParsedFile(Program program, File path) {
		
		return (ParsedFile)program.findElement(false, element -> {
			
			if (element instanceof ParsedFile) {
				final ParsedFile parsedFile = (ParsedFile)element;
				
				if (parsedFile.getFile().getPath().getAbsoluteFile().equals(path.getAbsoluteFile())) {
					return true;
				}
			}
			
			return false;
		});
		
		
	}
	
	private static class Element {
		private final BaseASTElement astElement;
		private final ISourceToken token;
		
		Element(BaseASTElement astElement, ISourceToken token) {
			this.astElement = astElement;
			this.token = token;
		}
	}
	
	@Override
	public void iterate(CompilationUnit sourceFile, SourceTokenVisitor visitor) {

		final ArrayStack<Element> stack = new ArrayStack<>();
		
		final Stack<Element> stackWrapper = new StackDelegator<Element>(stack) {

			@Override
			public void push(Element element) {

				super.push(element);

				if (!element.astElement.isPlaceholderElement()) {
					visitor.onPush(element.token);
				}
			}

			@Override
			public Element pop() {

				final Element element = super.pop();
				
				if (!element.astElement.isPlaceholderElement()) {
					visitor.onPop(element.token);
				}
				
				return element;
			}
		};

		sourceFile.iterateNodeFirstWithStack(
				stackWrapper,
				
				baseASTElement -> new Element(
						baseASTElement,
						baseASTElement.isPlaceholderElement() ? null : makeSourceToken(baseASTElement)),
				
				new ASTVisitor() {
			
			@Override
			public void onElement(BaseASTElement element) {
				
				if (stack.isEmpty()) {
					
					if (element.isPlaceholderElement()) {
						throw new IllegalStateException();
					}
					
					visitor.onToken(makeSourceToken(element));
				}
				else {
					
					final Element e = stack.get();
					
					if (e.token != null) {
						visitor.onToken(e.token);
					}
					else {
						if (!e.astElement.isPlaceholderElement()) {
							throw new IllegalStateException();
						}
					}
				}
			}
		});
	}


	@Override
	public CompilationUnit getCompilationUnit(ParsedFile sourceFile) {
		return sourceFile.getParsed();
	}

	@Override
	public ISourceToken getTokenAt(CompilationUnit compilationUnit, long offset) {

		final BaseASTElement found = compilationUnit.findElement(true, element -> {

			final Context context = element.getContext();

			return     !element.isPlaceholderElement()
					&& offset >= context.getStartOffset()
					&& offset <= context.getEndOffset();
		});
		
		return found != null ? makeSourceToken(found) : null;
	}
	
	private static SourceToken makeSourceToken(BaseASTElement element) {
		
		Objects.requireNonNull(element);

		final SourceTokenType sourceTokenType;
		
		if (element instanceof ClassDeclarationName) {
			sourceTokenType = SourceTokenType.CLASS_DECLARATION_NAME;
		}
		else if (element instanceof InterfaceDeclarationName) {
			sourceTokenType = SourceTokenType.INTERFACE_DECLARATION_NAME;
		}
		else {
			sourceTokenType = SourceTokenType.UNKNOWN;
		}
		
		final Context context = element.getContext();

		return new SourceToken(
				sourceTokenType,
				context != null ? context.getStartOffset() : -1L,
				context != null ? context.getText().length() : 0L,
				element);

	}
}
