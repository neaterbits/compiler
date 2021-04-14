package dev.nimbler.compiler.ast.objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.util.IdentityKey;
import com.neaterbits.util.parse.context.Context;
import com.neaterbits.util.parse.context.FullContext;

import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.TokenSequenceNoGenerator;

public class CompilationUnit extends CompilationCodeLines {

	private final ASTList<Import> imports;
	private final Map<Integer, BaseASTElement> elementsByParseTreeRef;
	private final Map<IdentityKey<BaseASTElement>, Integer> parseTreeRefsByElement;
	private final FullContextProvider fullContextProvider;

	public CompilationUnit(
	        Context context,
	        List<Import> imports,
	        List<CompilationCode> code,
	        FullContextProvider fullContextProvider) {
	    
		super(context, code);

		this.imports = makeList(imports);
		this.elementsByParseTreeRef = new HashMap<>();
		this.parseTreeRefsByElement = new HashMap<>();
		this.fullContextProvider = fullContextProvider;

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
	
	public FullContext makeFullContext(Context context) {
	    return fullContextProvider.makeFullContext(context);
	}
	
	public String getTokenString(int parseTreeTokenRef) {
	    
       final Context context = getElementFromParseTreeRef(parseTreeTokenRef).getContext();
     
       return getTokenString(context);
	}

    public String getTokenString(Context context) {

       return fullContextProvider.getText(context);
	}
	
	public int getTokenLength(int parseTreeTokenRef) {

	    final Context context = getElementFromParseTreeRef(parseTreeTokenRef).getContext();
	    
	    return fullContextProvider.getLength(context);
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
