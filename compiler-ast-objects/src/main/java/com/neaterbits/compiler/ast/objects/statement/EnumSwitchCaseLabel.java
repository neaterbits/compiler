package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

public final class EnumSwitchCaseLabel extends SwitchCaseLabel {

	private final ASTSingle<EnumConstant> enumConstant;
	
	public EnumSwitchCaseLabel(Context context, Keyword keyword, EnumConstant enumConstant) {
		super(context, keyword);
	
		Objects.requireNonNull(enumConstant);
		
		this.enumConstant = makeSingle(enumConstant);
	}

	public String getEnumConstant() {
		return enumConstant.get().getEnumConstant();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ENUM_SWITCH_CASE_LABEL;
	}

	@Override
	public <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param) {
		return visitor.onEnum(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		super.doRecurse(recurseMode, iterator);
		
		doIterate(enumConstant, recurseMode, iterator);
	}
}
