package com.neaterbits.compiler.ast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.util.Context;

public class CompilationUnit extends CompilationCodeLines {

	private final ASTList<Import> imports;
	private final Map<Integer, BaseASTElement> elementsByParseTreeRef;

	public CompilationUnit(Context context, List<Import> imports, List<CompilationCode> code) {
		super(context, code);
		
		this.imports = makeList(imports);
		this.elementsByParseTreeRef = new HashMap<>();
		
		this.iterateNodeFirst(e -> {
		
			if (!e.isPlaceholderElement()) {
				
				final int tokenSequenceNo = e.getContext().getTokenSequenceNo();
				
				if (tokenSequenceNo < 0) {
					throw new IllegalArgumentException("No sequence no for token " + e.getClass().getSimpleName());
				}
				
				elementsByParseTreeRef.put(tokenSequenceNo, e);
			}
		});
	}

	public ASTList<Import> getImports() {
		return imports;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(imports, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}

	public BaseASTElement getElementFromParseTreeRef(int parseTreeRef) {
		return elementsByParseTreeRef.get(parseTreeRef);
	}
	
	public int getParseTreeRefFromElement(BaseASTElement element) {
		
		final Context context = element.getContext();
		
		final int sequenceNo = context.getTokenSequenceNo();
		
		if (sequenceNo < 0) {
			throw new IllegalStateException();
		}
		
		return sequenceNo;
	}

}
