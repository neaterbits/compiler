package dev.nimbler.ide.changehistory;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.jutils.coll.UnmodifiableMapOfList;

import dev.nimbler.ide.model.text.TextEdit;

public interface HistoricChange {

    List<Path> getAdded();

    List<Path> getRemoved();

    Map<Path, Path> getMoved();

    UnmodifiableMapOfList<Path, TextEdit> getEdited();
}
