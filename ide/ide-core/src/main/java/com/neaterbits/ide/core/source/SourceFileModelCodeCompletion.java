package com.neaterbits.ide.core.source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.model.common.ISourceToken;
import com.neaterbits.ide.common.model.source.CodeCompletion;
import com.neaterbits.ide.common.model.source.CodeProposal;
import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.model.text.TextModel;

public class SourceFileModelCodeCompletion implements CodeCompletion {

	private final SourceFileModel sourceFileModel;
	// private final TextModel textModel;

	public SourceFileModelCodeCompletion(SourceFileModel sourceFileModel, TextModel textModel) {

		Objects.requireNonNull(sourceFileModel);
		Objects.requireNonNull(textModel);
		
		this.sourceFileModel = sourceFileModel;
		// this.textModel = textModel;
	}

	/*
	private Text getTokenText(ISourceToken token) {
		return textModel.getTextRange(token.getStartOffset(), token.getLength());
	}

	private String getTokenString(ISourceToken token) {
		return getTokenText(token).asString();
	}
	*/

	@Override
	public List<CodeProposal> getProposals(long offset) {

		final ISourceToken sourceToken = sourceFileModel.getSourceTokenAt(offset);
		
		final List<CodeProposal> proposals;
		
		if (sourceToken == null) {
			proposals = Collections.emptyList();
		}
		else {
			
			proposals = new ArrayList<>();
			
			switch (sourceToken.getTokenType()) {
			case CLASS_REFERENCE_NAME:
				break;
				
			case METHOD_REFERENCE_NAME:
				break;
			
				
			default:
				throw new UnsupportedOperationException();
			}
		}
		
		return proposals;
	}
}
