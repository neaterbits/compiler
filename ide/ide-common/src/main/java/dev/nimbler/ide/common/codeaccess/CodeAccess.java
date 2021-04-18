package dev.nimbler.ide.common.codeaccess;

import dev.nimbler.ide.common.model.codemap.CodeMapModel;

public interface CodeAccess extends SourceParseAccess, ProjectsAccess {

	CodeMapModel getCodeMapModel();
}
