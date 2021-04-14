package com.neaterbits.ide.component.common.language.compilercommon;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.compiler.model.common.IType;
import com.neaterbits.compiler.model.common.ProgramModel;
import com.neaterbits.compiler.model.common.ResolvedTypes;
import com.neaterbits.compiler.model.common.SourceTokenType;
import com.neaterbits.compiler.model.common.SourceTokenVisitor;
import com.neaterbits.compiler.model.common.TypeMemberVisitor;
import com.neaterbits.compiler.model.common.VariableScope;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.language.codemap.compiler.CompilerCodeMapGetters;

public final class CompilerSourceFileModel implements SourceFileModel {

	private final ProgramModel<?, CompilationUnit> programModel;
	private final CompilationUnit sourceFile;
	private final List<CompileError> parserErrors;
	private final ResolvedTypes resolvedTypes;
	private final int sourceFileNo;
	private final CompilerCodeMapGetters codeMap;
	
	public CompilerSourceFileModel(
			ProgramModel<?, CompilationUnit> programModel,
			CompilationUnit sourceFile,
			List<CompileError> parserErrors,
			ResolvedTypes resolvedTypes,
			int sourceFileNo,
			CompilerCodeMapGetters codeMap) {

		Objects.requireNonNull(programModel);
		Objects.requireNonNull(sourceFile);
		Objects.requireNonNull(resolvedTypes);
		Objects.requireNonNull(codeMap);
		
		this.programModel = programModel;
		this.sourceFile = sourceFile;
		this.parserErrors = parserErrors != null
				? Collections.unmodifiableList(parserErrors)
				: null;
				
		this.resolvedTypes = resolvedTypes;
		this.sourceFileNo = sourceFileNo;
		this.codeMap = codeMap;
	}

	@Override
	public void iterate(SourceTokenVisitor visitor, boolean visitPlaceholderElements) {
		programModel.iterate(sourceFile, visitor, resolvedTypes, visitPlaceholderElements);
	}

	@Override
	public void iterate(long offset, long length, SourceTokenVisitor visitor, boolean visitPlaceholderElements) {

		iterate(token -> {
			if (token.getStartOffset() >= offset && token.getStartOffset() < offset + length) {
				visitor.onToken(token);
			}
		},
		visitPlaceholderElements);
	}

	@Override
    public void iterateTypeMembers(TypeMemberVisitor typeMemberVisitor) {

	    programModel.iterateTypeMembers(sourceFile, typeMemberVisitor);
    }

    @Override
	public ISourceToken getSourceTokenAt(long offset) {

		final ISourceToken token = programModel.getTokenAtOffset(sourceFile, offset, resolvedTypes);
		
		if (token.getTokenType() == SourceTokenType.VARIABLE_REFERENCE) {
			
		}
		
		return token;
	}

	@Override
	public List<CompileError> getParserErrors() {
		return parserErrors;
	}

	
	@Override
	public VariableScope getVariableScope(ISourceToken token) {

		if (token.getTokenType() != SourceTokenType.VARIABLE_REFERENCE) {
			throw new IllegalArgumentException();
		}
		
		final int refToken = codeMap.getTokenForParseTreeRef(sourceFileNo, token.getParseTreeReference());
		
		final VariableScope scope;
		
		if (refToken >= 0) {
		
			final int declarationToken = codeMap.getVariableDeclarationTokenReferencedFrom(refToken);
	
			if (declarationToken >= 0) {
				
				final int declarationParseTreeRef = codeMap.getParseTreeRefForToken(declarationToken);

				final ISourceToken declarationSourceToken = programModel.getTokenAtParseTreeRef(sourceFile, declarationParseTreeRef, resolvedTypes);

				if (declarationSourceToken != null) {

					switch (declarationSourceToken.getTokenType()) {
					case INSTANCE_VARIABLE_DECLARATION_NAME:
						scope = VariableScope.MEMBER;
						break;

					case STATIC_VARIABLE_DECLARATION_NAME:
						scope = VariableScope.STATIC_MEMBER;
						break;
						
					case CALL_PARAMETER_DECLARATION_NAME:
						scope = VariableScope.PARAMETER;
						break;
						
					case LOCAL_VARIABLE_DECLARATION_NAME:
						scope = VariableScope.LOCAL;
						break;
						
					default:
						throw new UnsupportedOperationException();
					}
				}
				else{
					scope = null;
				}
			}
			else {
				scope = null;
			}
		}
		else {
			scope = null;
		}
		
		return scope;
	}

	@Override
	public IType getVariableType(ISourceToken token) {
		
		if (!token.getTokenType().isVariable()) {
			throw new IllegalArgumentException();
		}
		
		
		// TODO Auto-generated method stub
		return null;
	}
}
