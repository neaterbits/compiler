package dev.nimbler.ide.core.tasks;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

final class ModuleDep extends Dep<ProjectModuleResourcePath> {

    ModuleDep(ProjectModuleResourcePath from, ProjectModuleResourcePath to) {
        super(from, to);
    }
}
