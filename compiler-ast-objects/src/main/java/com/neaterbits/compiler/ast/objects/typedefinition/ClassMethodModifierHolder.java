package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.typedefinition.ClassMethodModifier;
import com.neaterbits.compiler.types.typedefinition.ClassMethodModifierVisitor;
import com.neaterbits.compiler.util.Context;

public final class ClassMethodModifierHolder extends BaseModifierHolder<ClassMethodModifier>
			implements ClassMethodModifier {

	public ClassMethodModifierHolder(Context context, ClassMethodModifier delegate) {
		super(context, delegate);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_METHOD_MODIFIER_HOLDER;
	}

	@Override
	public <T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
