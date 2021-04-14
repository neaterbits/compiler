package dev.nimbler.compiler.resolver.passes;

import java.util.ArrayList;
import java.util.List;

import dev.nimbler.compiler.model.common.NamespaceVisitor;

public abstract class BaseNamespaceVisitor implements NamespaceVisitor {

    private final List<String> currentNamespace;

    protected BaseNamespaceVisitor() {
        this.currentNamespace = new ArrayList<>();
    }

    @Override
    public final void onNamespaceStart() {
        
        if (!currentNamespace.isEmpty()) {
            throw new IllegalStateException();
        }
    }

    @Override
    public final void onNamespacePart(CharSequence part) {

        currentNamespace.add(part.toString());
    }

    @Override
    public final void onNamespaceEnd() {

        currentNamespace.clear();
    }

    protected final List<String> getCurrentNamespace() {
        return currentNamespace;
    }
}
