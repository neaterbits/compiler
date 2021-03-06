package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BasePlaceholderASTElement;
import dev.nimbler.compiler.ast.objects.annotation.Annotation;
import dev.nimbler.compiler.ast.objects.list.ASTList;

public abstract class BaseModifiers<MODIFIER, MODIFIER_HOLDER extends BaseModifierHolder<MODIFIER>>
			extends BasePlaceholderASTElement {

    private final ASTList<Annotation> annotations;
	private final ASTList<MODIFIER_HOLDER> modifiers;

	protected BaseModifiers(List<Annotation> annotations, List<MODIFIER_HOLDER> modifiers) {

		Objects.requireNonNull(modifiers);
		
        this.annotations = annotations != null ? makeList(annotations) : null;
		this.modifiers = makeList(modifiers);
	}
	
    public final ASTList<Annotation> getAnnotations() {
        return annotations;
    }
	
	public final ASTList<MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}

	@SuppressWarnings("unchecked")
	public final <T extends MODIFIER> T getModifier(Class<T> cl) {

		Objects.requireNonNull(cl);

		final MODIFIER_HOLDER modifierHolder = modifiers.find(modifier -> modifier.getDelegate().getClass().equals(cl));
		
		return modifierHolder != null ? (T)modifierHolder.getDelegate() : null;
	}
	
	public final boolean isEmpty() {
	    return modifiers.isEmpty();
	}
	
	public final int count() {
	    return modifiers.size();
	}
	
	public final boolean hasModifier(Class<? extends MODIFIER> cl) {
		return getModifier(cl) != null;
	}

	public final boolean hasModifier(MODIFIER modifier) {
        return modifiers.find(holder -> holder.getModifier().equals(modifier)) != null;
    }

	@Override
	protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(modifiers, recurseMode, iterator);
	}
}
