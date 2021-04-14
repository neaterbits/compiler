package dev.nimbler.compiler.ast.objects.typedefinition;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.ClassMethodModifier;
import dev.nimbler.compiler.types.typedefinition.ClassMethodModifierVisitor;

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
