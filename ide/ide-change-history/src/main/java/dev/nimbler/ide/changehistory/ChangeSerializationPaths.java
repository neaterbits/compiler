package dev.nimbler.ide.changehistory;

import java.nio.file.Path;

class ChangeSerializationPaths {

    private static final String REFACTOR_XML_NAME = "refactor.xml";

    static final Path REFACTOR_XML = Path.of(REFACTOR_XML_NAME);

    private static final String HISTORY_MOVE_XML_NAME = "history_move.xml";

    static final Path HISTORY_MOVE_XML = Path.of(HISTORY_MOVE_XML_NAME);

    private static final String EDITS_XML_NAME = "edits.xml";
    
    static final Path EDITS_XML = Path.of(EDITS_XML_NAME);
}
