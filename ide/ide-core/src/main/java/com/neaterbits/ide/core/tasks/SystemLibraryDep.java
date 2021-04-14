package com.neaterbits.ide.core.tasks;

import com.neaterbits.build.types.resource.ProjectModuleResourcePath;
import com.neaterbits.language.common.typesources.DependencyFile;

final class SystemLibraryDep extends Dep<DependencyFile> {

    SystemLibraryDep(ProjectModuleResourcePath from, DependencyFile to) {
        super(from, to);
    }
}
