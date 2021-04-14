package com.neaterbits.ide.core.tasks;

import java.util.Objects;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

abstract class Dep<T> {

    private final ProjectModuleResourcePath from;
    private final T to;

    Dep(ProjectModuleResourcePath from, T to) {
        
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        
        this.from = from;
        this.to = to;
    }

    final ProjectModuleResourcePath getFrom() {
        return from;
    }

    final T getTo() {
        return to;
    }
}
