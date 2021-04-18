package dev.nimbler.ide.core.tasks;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.language.common.typesources.DependencyFile;

final class SystemLibraryDep extends Dep<DependencyFile> {

    SystemLibraryDep(ProjectModuleResourcePath from, DependencyFile to) {
        super(from, to);
    }
}
