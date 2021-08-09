package dev.nimbler.ide.changehistory.filestorage.zip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

import dev.nimbler.ide.changehistory.filestorage.FileRetrieval;
import dev.nimbler.ide.changehistory.filestorage.FileStorage;
import dev.nimbler.ide.changehistory.filestorage.FileStorageFactory;

public final class ZipFileStorageFactory implements FileStorageFactory {

    private static final String PREV_STATE_NAME = "prevstate.zip";
    private static final String EDITS_NAME = "edits.zip";

    private static class ZipFileStorage implements FileStorage {
        
        private final ZipFileWriter zipFileWriter;
        
        ZipFileStorage(Path directory, String name) throws IOException {

            this.zipFileWriter = new ZipFileWriter(directory.resolve(name));
        }
        
        @Override
        public OutputStream addFile(Path filePath) throws IOException {

            return zipFileWriter.addFile(filePath);
        }

        @Override
        public void close() throws Exception {

            zipFileWriter.close();
        }
    }
    
    private static class ZipFileRetrieval implements FileRetrieval {

        private final ZipFileReader zipFileReader;

        ZipFileRetrieval(Path directory, String name) throws IOException {

            this.zipFileReader = new ZipFileReader(directory.resolve(name));
        }

        @Override
        public InputStream retrieveFile(Path filePath) throws IOException {

            return zipFileReader.readFile(filePath);
        }
        
        @Override
        public void close() throws Exception {

            zipFileReader.close();
        }
    }
    
    @Override
    public FileStorage createPrevStateStorage(Path directory) throws IOException {
        
        return new ZipFileStorage(directory, PREV_STATE_NAME);
    }

    @Override
    public FileStorage createEditsStorage(Path directory) throws IOException {

        return new ZipFileStorage(directory, EDITS_NAME);
    }

    @Override
    public FileRetrieval createPrevStateRetrieval(Path directory) throws IOException {

        return new ZipFileRetrieval(directory, PREV_STATE_NAME);
    }

    @Override
    public FileRetrieval createEditsRetrieval(Path directory) throws IOException {

        return new ZipFileRetrieval(directory, EDITS_NAME);
    }
}
