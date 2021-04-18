package dev.nimbler.ide.core.tasks;

import dev.nimbler.build.types.compile.FileCompilation;
import dev.nimbler.build.types.resource.ProjectModuleResourcePath;

final class FileCompilationDep extends Dep<FileCompilation> {

    FileCompilationDep(ProjectModuleResourcePath from, FileCompilation to) {
        super(from, to);
    }
}
