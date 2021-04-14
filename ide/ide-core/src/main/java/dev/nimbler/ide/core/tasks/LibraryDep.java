package dev.nimbler.ide.core.tasks;

import dev.nimbler.build.types.dependencies.LibraryDependency;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

final class LibraryDep extends Dep<LibraryDependency> {

    LibraryDep(ProjectModuleResourcePath from, LibraryDependency to) {
        super(from, to);
    }
}
