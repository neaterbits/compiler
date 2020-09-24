package com.neaterbits.compiler.resolver.passes.typefinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class NamespaceScope extends TypeFinderScope {

    private final List<String> parts;

    NamespaceScope() {
        this.parts = new ArrayList<>();
    }

    void addPart(String part) {

        Objects.requireNonNull(part);

        parts.add(part);
    }

    String [] getParts() {
        return parts.toArray(new String[parts.size()]);
    }
}
