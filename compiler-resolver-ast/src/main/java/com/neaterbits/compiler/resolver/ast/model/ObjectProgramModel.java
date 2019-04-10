package com.neaterbits.compiler.resolver.ast.model;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTVisitor;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.FieldNameDeclaration;
import com.neaterbits.compiler.ast.Import;
import com.neaterbits.compiler.ast.ImportName;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.Namespace;
import com.neaterbits.compiler.ast.NamespaceDeclaration;
import com.neaterbits.compiler.ast.Program;
import com.neaterbits.compiler.ast.parser.ParsedFile;
import com.neaterbits.compiler.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.ComplexTypeDefinition;
import com.neaterbits.compiler.ast.typedefinition.FieldModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.InterfaceMethodName;
import com.neaterbits.compiler.ast.typedefinition.InterfaceModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.VariableModifierHolder;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.resolver.ScopedNameResolver;
import com.neaterbits.compiler.resolver.TypesMap;
import com.neaterbits.compiler.util.ArrayStack;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.Stack;
import com.neaterbits.compiler.util.StackDelegator;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.ISourceToken;
import com.neaterbits.compiler.util.model.ProgramModel;
import com.neaterbits.compiler.util.model.ResolvedTypes;
import com.neaterbits.compiler.util.model.SourceToken;
import com.neaterbits.compiler.util.model.SourceTokenType;
import com.neaterbits.compiler.util.model.SourceTokenVisitor;
import com.neaterbits.compiler.util.model.TypeImportVisitor;

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
	public void iterate(CompilationUnit sourceFile, SourceTokenVisitor visitor, ResolvedTypes resolvedTypes) {

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
						baseASTElement.isPlaceholderElement() ? null : makeSourceToken(baseASTElement, sourceFile, resolvedTypes)),
				
				new ASTVisitor() {
			
			@Override
			public void onElement(BaseASTElement element) {
				
				if (stack.isEmpty()) {
					
					if (element.isPlaceholderElement()) {
						throw new IllegalStateException();
					}
					
					visitor.onToken(makeSourceToken(element, sourceFile, resolvedTypes));
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
	public ISourceToken getTokenAt(CompilationUnit compilationUnit, long offset, ResolvedTypes resolvedTypes) {

		final BaseASTElement found = compilationUnit.findElement(true, element -> {

			final Context context = element.getContext();

			return     !element.isPlaceholderElement()
					&& offset >= context.getStartOffset()
					&& offset <= context.getEndOffset();
		});
		
		return found != null ? makeSourceToken(found, compilationUnit, resolvedTypes) : null;
	}
	
	
	@Override
	public void iterateTypeImports(CompilationUnit sourceFile, TypeImportVisitor visitor) {
		for (Import typeImport : sourceFile.getImports()) {
			typeImport.visit(visitor);
		}
	}


	private SourceToken makeSourceToken(BaseASTElement element, CompilationUnit compilationUnit, ResolvedTypes resolvedTypes) {
		
		Objects.requireNonNull(element);

		final SourceTokenType sourceTokenType;
		TypeName typeName = null;
		
		if (element instanceof Keyword) {
			sourceTokenType = SourceTokenType.KEYWORD;
		}
		else if (   element instanceof InterfaceModifierHolder
				 || element instanceof ClassModifierHolder
				 || element instanceof ClassMethodModifierHolder
				 || element instanceof FieldModifierHolder
				 || element instanceof VariableModifierHolder) {
			
			sourceTokenType = SourceTokenType.KEYWORD;
			
		}
		else if (element instanceof NamespaceDeclaration) {
			sourceTokenType = SourceTokenType.NAMESPACE_DECLARATION_NAME;
		}
		else if (element instanceof ImportName) {
			sourceTokenType = SourceTokenType.IMPORT_NAME;
		}
		else if (element instanceof ClassDeclarationName) {
			sourceTokenType = SourceTokenType.CLASS_DECLARATION_NAME;
		}
		else if (element instanceof FieldNameDeclaration) {
			sourceTokenType = SourceTokenType.INSTANCE_VARIABLE_DECLARATION_NAME;
		}
		else if (element instanceof InterfaceDeclarationName) {
			sourceTokenType = SourceTokenType.INTERFACE_DECLARATION_NAME;
		}
		else if (element instanceof InterfaceMethodName) {
			sourceTokenType = SourceTokenType.METHOD_DECLARATION_NAME;
		}
		else if (element instanceof ResolveLaterTypeReference) {

			// Resolve from already resolved types
			final ResolveLaterTypeReference typeReference = (ResolveLaterTypeReference)element;
			final TypesMap<TypeName> compiledTypesMap = new TypesMap<TypeName>() {

				@Override
				public TypeName lookupByScopedName(ScopedName scopedName) {
					return resolvedTypes.lookup(scopedName);
				}
			};

			final Namespace namespace = (Namespace)compilationUnit.findElement(false, e -> e instanceof Namespace);
			final ComplexTypeDefinition<?, ?> definition = (ComplexTypeDefinition<?, ?>)compilationUnit.findElement(false, e -> e instanceof ComplexTypeDefinition<?, ?>);

			if (definition != null) {
			
				final ScopedName referencedFrom = new ScopedName(
						namespace != null
							? Arrays.asList(namespace.getParts())
							: null,
						definition.getNameString());
				
				final TypeName resolved = ScopedNameResolver.resolveScopedName(
						typeReference.getTypeName(),
						null,
						compilationUnit,
						this,
						referencedFrom,
						compiledTypesMap);
				
				if (resolved != null) {
					sourceTokenType = SourceTokenType.CLASS_REFERENCE_NAME;
					typeName = resolved;
				}
				else {
					sourceTokenType = SourceTokenType.UNKNOWN;
				}
			}
			else {
				sourceTokenType = SourceTokenType.UNKNOWN;
			}
		}
		else {
			sourceTokenType = SourceTokenType.UNKNOWN;
		}
		
		final Context context = element.getContext();

		return new SourceToken(
				sourceTokenType,
				context.getStartOffset(),
				context.getEndOffset() - context.getStartOffset() + 1,
				typeName,
				element.getClass().getSimpleName());
	}
}
