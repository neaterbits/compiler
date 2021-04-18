package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.project.model.MavenResource;

public final class StackResources extends StackBase {

    private final List<MavenResource> resources;

    StackResources(Context context) {
        super(context);

        this.resources = new ArrayList<>();
    }

    void add(MavenResource resource) {

        Objects.requireNonNull(resource);

        resources.add(resource);
    }
    
    List<MavenResource> getResources() {
        return resources;
    }
}
