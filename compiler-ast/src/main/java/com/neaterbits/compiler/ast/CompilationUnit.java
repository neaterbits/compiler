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
				elementsByParseTreeRef.put(e.getContext().getStartOffset(), e);
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
		
		return context.getTokenSequenceNo();
	}

}
