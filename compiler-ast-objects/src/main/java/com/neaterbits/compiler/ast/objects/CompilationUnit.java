package com.neaterbits.compiler.ast.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.util.TokenSequenceNoGenerator;
import com.neaterbits.util.IdentityKey;
import com.neaterbits.util.parse.context.Context;

public class CompilationUnit extends CompilationCodeLines {

	private final ASTList<Import> imports;
	private final Map<Integer, BaseASTElement> elementsByParseTreeRef;
	private final Map<IdentityKey<BaseASTElement>, Integer> parseTreeRefsByElement;

	public CompilationUnit(Context context, List<Import> imports, List<CompilationCode> code) {
		super(context, code);

		this.imports = makeList(imports);
		this.elementsByParseTreeRef = new HashMap<>();
		this.parseTreeRefsByElement = new HashMap<>();

		final TokenSequenceNoGenerator gen = new TokenSequenceNoGenerator();

		this.iterateNodeFirst(e -> {

			if (!e.isPlaceholderElement()) {

				final int tokenSequenceNo = gen.getNextTokenSequenceNo();

				elementsByParseTreeRef.put(tokenSequenceNo, e);
				parseTreeRefsByElement.put(new IdentityKey<>(e), tokenSequenceNo);
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
	    
	    Objects.requireNonNull(element);
	    
	    final int parseTreeRef = parseTreeRefsByElement.get(new IdentityKey<>(element));

		return parseTreeRef;
	}

	public void replace(BaseASTElement toReplace, BaseASTElement toReplaceWith) {

	    Objects.requireNonNull(toReplace);
	    Objects.requireNonNull(toReplaceWith);

        toReplace.replaceWith(toReplaceWith);

        final int parseTreeElementRef = parseTreeRefsByElement.remove(new IdentityKey<BaseASTElement>(toReplace));

        parseTreeRefsByElement.put(new IdentityKey<BaseASTElement>(toReplaceWith), parseTreeElementRef);

        elementsByParseTreeRef.replace(parseTreeElementRef, toReplaceWith);
	}
}
