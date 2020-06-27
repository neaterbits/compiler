package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.annotation.Annotation;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.ClassModifier;

public final class ClassModifiers extends BaseModifiers<ClassModifier, ClassModifierHolder> {
	
    private final ASTList<Annotation> annotations;
    
	public ClassModifiers(List<Annotation> annotations, List<ClassModifierHolder> modifiers) {
		super(modifiers);
		
		this.annotations = annotations != null ? makeList(annotations) : null;
	}

	public ASTList<Annotation> getAnnotations() {
        return annotations;
    }

    @Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_MODIFIERS;
	}
}
