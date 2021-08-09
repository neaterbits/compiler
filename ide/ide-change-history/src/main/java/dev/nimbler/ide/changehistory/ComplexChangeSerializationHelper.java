package dev.nimbler.ide.changehistory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import javax.xml.stream.XMLStreamException;

import org.jutils.IOUtils;
import org.jutils.PathUtil;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.changehistory.changeoutput.ChangeHistoryStorageFactory;
import dev.nimbler.ide.changehistory.filestorage.FileStorage;

/**
 * Algorithm for serializing a change, kept in separate helper class
 */
class ComplexChangeSerializationHelper {

    static void serialize(
            SourceFileComplexChange complexChange,
            ChangeHistoryStorageFactory changeOutputFactory,
            Path ideBasePath) throws IOException, XMLStreamException {

        serializePrevState(complexChange, changeOutputFactory, ideBasePath);

        if (complexChange.getChangeReason() == ChangeReason.REFACTOR) {

            RefactorSerializationHelper.serializeRefactor(
                    complexChange,
                    changeOutputFactory,
                    ideBasePath);
        }
        else if (complexChange.getChangeReason().isHistoryMove()) {

            RefactorSerializationHelper.serializeHistoryMove(
                    complexChange,
                    changeOutputFactory,
                    ideBasePath);
        }

        EditsSerializationHelper.serializeEdits(complexChange, changeOutputFactory, ideBasePath);
    }

    private static void serializePrevState(
            SourceFileComplexChange change,
            ChangeHistoryStorageFactory changeOutputFactory,
            Path ideBasePath) throws IOException {
        
        try (FileStorage output = changeOutputFactory.createPrevStateChangeOutput()) {
            
            if (change.getRemoveFiles() != null) {
                serializeFiles(change.getRemoveFiles(), output, ideBasePath);
            }
            
            if (change.getMoveFiles() != null) {
                serializeFiles(change.getMoveFiles().keySet(), output, ideBasePath);
            }
            
            if (change.getEdits() != null) {
                serializeFiles(change.getEdits().keys(), output, ideBasePath);
            }
        }
        catch (IOException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new IOException("Exception on close", ex);
        }
    }
    
    private static void serializeFiles(
            Iterable<SourceFileResourcePath> files,
            FileStorage output,
            Path ideBasePath) throws IOException {
        
        for (SourceFileResourcePath sourceFileResourcePath : files) {

            final File sourceFile = sourceFileResourcePath.getFile();
            final Path relativePath = getRelativePath(sourceFileResourcePath, ideBasePath);

            try (OutputStream outputStream = output.addFile(relativePath)) {
                IOUtils.applyAll(sourceFile, outputStream);
            }
        }
    }

    static String getRelativePathString(SourceFileResourcePath sourceFileResourcePath, Path ideBasePath) {
        
        return PathUtil.getForwardSlashPathString(getRelativePath(sourceFileResourcePath, ideBasePath));
    }

    private static Path getRelativePath(SourceFileResourcePath sourceFileResourcePath, Path ideBasePath) {
        
        return getRelativePath(sourceFileResourcePath.getFile(), ideBasePath);
    }

    private static Path getRelativePath(File sourceFile, Path ideBasePath) {

        final Path sourceFilePath = sourceFile.toPath();
        
        final Path relativePath = ideBasePath.relativize(sourceFilePath);
    
        return relativePath;
    }
}
