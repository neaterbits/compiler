package dev.nimbler.compiler.ast.objects.variables;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.util.name.Name;

public final class VarNameDeclaration extends BaseASTElement {

	private final VarName varName;
	
	public VarNameDeclaration(Context context, String name) {
		super(context);

		Objects.requireNonNull(name);
		
		Name.check(name);
		
		this.varName = new VarName(name);
	}

	public VarName getVarName() {
		return varName;
	}
	
	public String getName() {
	    return varName.getName();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.VAR_NAME_DECLARATION;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
