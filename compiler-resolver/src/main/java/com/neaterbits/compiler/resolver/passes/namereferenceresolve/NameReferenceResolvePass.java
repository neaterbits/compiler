package com.neaterbits.compiler.resolver.passes.namereferenceresolve;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.codemap.compiler.CrossReferenceUpdater;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.CompiledAndResolvedFile;
import com.neaterbits.compiler.model.common.passes.MultiPass;
import com.neaterbits.compiler.resolver.UnknownReferenceError;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.parse.ScopesListener;

public class NameReferenceResolvePass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT, BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>
		extends MultiPass<
			CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>,
			CodeMapCompiledAndMappedFiles<COMPILATION_UNIT>> {

	
	private final CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel;
	
	public NameReferenceResolvePass(CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel) {

		Objects.requireNonNull(compilationUnitModel);
		
		this.compilationUnitModel = compilationUnitModel;
	}

	@Override
	public CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> execute(CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> input) throws IOException {

		final List<CompileError> errors = new ArrayList<>();
		
		for (CompiledAndResolvedFile file : input.getFiles()) {
			
			final int sourceFileNo = input.getSourceFileNo(file);
			
			resolveFile(file.getParsedFile(), input.getCrossReferenceUpdater(), sourceFileNo, errors);
		}
		
		return input;
	}

	private void resolveFile(ParsedFile parsedFile, CrossReferenceUpdater crossReference, int sourceFile, List<CompileError> errors) {
		
		final Scopes scopes = new Scopes();
		final Primaries primaries = new Primaries();
		
		final COMPILATION_UNIT compilationUnit = parsedFile.getCompilationUnit();

		
		System.out.println("## resolveFile");

		
		final ScopesListener scopesLiestener = new ScopesListener() {

			@Override
			public void onClassStart(int classParseTreeRef) {

				scopes.push();
				
			}

			@Override
			public void onMethodScopeStart(int methodParseTreeRef) {

				crossReference.addToken(sourceFile, methodParseTreeRef);

				scopes.push();
			}

			@Override
			public void onBlockScopeStart(int blockRef) {

				scopes.push();
				
			}

			@Override
			public void onScopeVariableDeclaration(int variableDeclarationParseTreeRef, String name, TypeName type) {

				System.out.println("## variable declaration " + variableDeclarationParseTreeRef + " type " + type.getName());
				
				final VariableScope scope = scopes.getCurrentScope();

				final int declarationCrossReferenceToken = crossReference.addToken(sourceFile, variableDeclarationParseTreeRef);
				
				scope.addVariableDeclaration(variableDeclarationParseTreeRef, declarationCrossReferenceToken, name, type);
			}
			
			@Override
			public <T> void onNonPrimaryListNameReference(int nameParseTreeRef, String name) {

				final VariableDeclaration declaration = scopes.resolveVariableDeclaration(name);
				
				System.out.println("## found variable declaration " + declaration + " for " + name);

				if (declaration != null) {
					
					final int fromToken = crossReference.addToken(sourceFile, nameParseTreeRef);
					
					crossReference.addTokenVariableReference(fromToken, declaration.getTokenNo());
				}
				else {
					errors.add(new UnknownReferenceError("Unknown name " + name));
				}
			}

			@Override
			public <T> T onPrimaryListStart(T primaryList, int primaryListParseTreeRef, int size) {

				primaries.push(new PrimaryListEntry(primaryListParseTreeRef));
				
				return null;
			}

			@Override
			public <T> void onPrimaryListEnd(T primaryList, int primaryListParseTreeRef) {

				primaries.pop();
				
			}

			@Override
			public <T> void onPrimaryListNameReference(T primaryList, int nameParseTreeRef, String name) {
				
				final PrimaryListEntry entry = primaries.getEntry();
				
				if (entry.isEmpty()) {
					
					// Initial entry, eg a in a.b.c
					// Find name in scopes
					// final String name = compilationUnitModel.getTokenString(compilationUnit, nameParseTreeRef);
					final VariableDeclaration declaration = scopes.resolveVariableDeclaration(name);
					
					System.out.println("## found variable declaration" + declaration + " for " + name);
					
					if (declaration != null) {
						entry.addType(declaration.getType());
					}
					else {
						errors.add(new UnknownReferenceError("Unknown name " + name));
					}
				}
				else {
					
					// Field, eg b in a.b.c
					
				}
			}

			@Override
			public <T> void onSimpleVariableReference(T primaryList, int variableParseTreeRef) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public <T> T onArrayAccessReference(T primaryList, int arrayAccessParseTreeRef) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> T onFieldAccessReference(T primaryList, int fieldAccessParseTreeRef) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <T> T onStaticMemberReference(T primaryList, int staticMemberParseTreeRef) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onBlockScopeEnd(int blockParseTreeRef) {

				scopes.pop();
				
			}

			@Override
			public void onMethodScopeEnd(int methodParseTreeRef) {

				scopes.pop();
				
			}

			@Override
			public void onClassEnd(int classParseTreeRef) {

				scopes.pop();
				
			}
		};
		
		compilationUnitModel.iterateScopesAndVariables(compilationUnit, scopesLiestener);
	}
}
