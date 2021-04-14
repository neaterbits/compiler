package com.neaterbits.ide.core.tasks;

import com.neaterbits.build.types.dependencies.LibraryDependency;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

final class LibraryDep extends Dep<LibraryDependency> {

    LibraryDep(ProjectModuleResourcePath from, LibraryDependency to) {
        super(from, to);
    }
}
