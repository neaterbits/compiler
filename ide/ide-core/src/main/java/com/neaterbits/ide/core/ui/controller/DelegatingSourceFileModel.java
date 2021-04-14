package com.neaterbits.ide.core.ui.controller;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.compiler.model.common.IType;
import com.neaterbits.compiler.model.common.SourceTokenVisitor;
import com.neaterbits.compiler.model.common.TypeMemberVisitor;
import com.neaterbits.compiler.model.common.VariableScope;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.ide.common.model.source.SourceFileModel;

// Delegates and allows for swapping source file model on re-parse

final class DelegatingSourceFileModel implements SourceFileModel {

	private SourceFileModel delegate;

	DelegatingSourceFileModel() {
		this.delegate = null;
	}

	DelegatingSourceFileModel(SourceFileModel delegate) {
		this.delegate = delegate;
	}
	
	void setDelegate(SourceFileModel delegate) {
		
		System.out.println("## setDelegate");
		
		Objects.requireNonNull(delegate);
		
		if (this == delegate) {
			throw new IllegalArgumentException();
		}
	
		this.delegate = delegate;
	}

	@Override
	public void iterate(SourceTokenVisitor visitor, boolean visitPlaceholderElements) {

		if (delegate != null) {
			delegate.iterate(visitor, visitPlaceholderElements);
		}
	}

	@Override
	public void iterate(long offset, long length, SourceTokenVisitor visitor, boolean visitPlaceholderElements) {

		if (delegate != null) {
			delegate.iterate(offset, length, visitor, visitPlaceholderElements);
		}
	}

	@Override
    public void iterateTypeMembers(TypeMemberVisitor typeMemberVisitor) {

	    if (delegate != null) {
	        delegate.iterateTypeMembers(typeMemberVisitor);
	    }
    }

    @Override
	public VariableScope getVariableScope(ISourceToken token) {
		return delegate != null ? delegate.getVariableScope(token) : null;
	}

	@Override
	public ISourceToken getSourceTokenAt(long offset) {
		return delegate != null ? delegate.getSourceTokenAt(offset) : null;
	}

	@Override
	public IType getVariableType(ISourceToken token) {
		return delegate != null ? delegate.getVariableType(token) : null;
	}

	@Override
	public List<CompileError> getParserErrors() {
		return delegate != null ? delegate.getParserErrors() : Collections.emptyList();
	}
}

