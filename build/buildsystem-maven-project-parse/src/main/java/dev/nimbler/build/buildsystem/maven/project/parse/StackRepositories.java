package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.project.model.BaseMavenRepository;

final class StackRepositories<T extends BaseMavenRepository> extends StackBase {

    private final List<T> repositories;
    
    StackRepositories(Context context) {
        super(context);

        this.repositories = new ArrayList<>();
    }
    
    void add(T baseMavenRepository) {
        
        Objects.requireNonNull(baseMavenRepository);
        
        repositories.add(baseMavenRepository);
    }

    List<T> getRepositories() {
        return repositories;
    }
}
