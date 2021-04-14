package com.neaterbits.ide.core.tasks;

import com.neaterbits.build.types.compile.FileCompilation;
import com.neaterbits.build.types.resource.ProjectModuleResourcePath;

final class FileCompilationDep extends Dep<FileCompilation> {

    FileCompilationDep(ProjectModuleResourcePath from, FileCompilation to) {
        super(from, to);
    }
}
