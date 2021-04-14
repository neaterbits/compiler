package com.neaterbits.ide.common.model.source;

import java.util.List;

public interface CodeCompletion {

	List<CodeProposal> getProposals(long offset);
	
}
