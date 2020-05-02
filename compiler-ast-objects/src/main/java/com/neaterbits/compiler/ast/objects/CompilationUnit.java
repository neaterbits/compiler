package com.neaterbits.compiler.ast.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TokenSequenceNoGenerator;
import com.neaterbits.util.IdentityKey;

public class CompilationUnit extends CompilationCodeLines {

	private final ASTList<Import> imports;
	private final Map<Integer, BaseASTElement> elementsByParseTreeRef;
	private final Map<IdentityKey<BaseASTElement>, Integer> parseTreeRefsByLement;

	public CompilationUnit(Context context, List<Import> imports, List<CompilationCode> code) {
		super(context, code);
		
		this.imports = makeList(imports);
		this.elementsByParseTreeRef = new HashMap<>();
		this.parseTreeRefsByLement = new HashMap<>();
		
		final TokenSequenceNoGenerator gen = new TokenSequenceNoGenerator();

		this.iterateNodeFirst(e -> {
		
			if (!e.isPlaceholderElement()) {
			    
				
				final int tokenSequenceNo = gen.getNextTokenSequenceNo();
				
				elementsByParseTreeRef.put(tokenSequenceNo, e);
				parseTreeRefsByLement.put(new IdentityKey<>(e), tokenSequenceNo);
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
		
	    final int parseTreeRef = parseTreeRefsByLement.get(new IdentityKey<>(element));
	    
		return parseTreeRef;
	}
}
