package dev.nimbler.ide.changehistory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import org.jutils.StringUtils;
import org.jutils.Value;

import dev.nimbler.ide.changehistory.changeoutput.ChangeHistoryStorageFactory;
import dev.nimbler.ide.changehistory.filestorage.FileRetrieval;
import dev.nimbler.ide.changehistory.filestorage.FileStorage;
import dev.nimbler.ide.changehistory.filestorage.FileStorageFactory;
import dev.nimbler.ide.changehistory.filestorage.zip.ZipFileStorageFactory;

/**
 * Stores changes in directory of directories, utilizing UUIDs for directory names.
 * 
 * So
 * 
 * <basePath>/<top level dirs named by UUID>/<sub dirs named by UUID>/<files for one history change>
 *
 * Subdirs have at most changesPerDirectory number of entries.
 */

final class UUIDChangeStore implements ChangeStore {

    private static final String CHANGE_FILE_NAME = "changes.txt";
    
    private final Path basePath;
    private final int changesPerSubDirectory;

    private final FileStorageFactory fileStorageFactory;
    
    private final FileOutputStream changeFileOutputStream;
    
    private UUID currentTopLevelDirectoryUUID;
    
    /**
     * Create a new {@link UUIDChangeStore} in a base path
     * 
     * @param basePath the {@link Path} at which to add directories of directories of changes
     * @param changesPerSubDirectory the max number of changes per sub directory
     * 
     * @throws IOException
     */
    UUIDChangeStore(Path basePath, int changesPerSubDirectory)
            throws IOException {

        Objects.requireNonNull(basePath);
        
        this.basePath = basePath;
        this.changesPerSubDirectory = changesPerSubDirectory;
        
        this.fileStorageFactory = new ZipFileStorageFactory();

        // Scan for any path that has less than changesPerDirectory?
        
        final File changeFile = getChangeFile();
        
        if (changeFile.exists()) {
            this.currentTopLevelDirectoryUUID = scanLatestChangeUUID(changeFile);
        }

        this.changeFileOutputStream = new FileOutputStream(changeFile, true);
    }

    @Override
    public ChangeHistoryStorageFactory prepare(SourceFileComplexChange change) throws IOException {
        
        Objects.requireNonNull(change);
        
        final File basePath;
        
        if (currentTopLevelDirectoryUUID == null
                || getFileOfTopLevelDirectory(currentTopLevelDirectoryUUID).list().length >= changesPerSubDirectory) {
            
            this.currentTopLevelDirectoryUUID = genUUID();

            basePath = getFileOfTopLevelDirectory(currentTopLevelDirectoryUUID);

            if (!basePath.mkdirs()) {
                throw new IllegalStateException();
            }
        }
        else {
            basePath = getFileOfTopLevelDirectory(currentTopLevelDirectoryUUID);
        }

        final Path directory = basePath.toPath();
        
        final UUID subDirectoryUUID = genUUID();
        final String subDirectoryUUIDString = subDirectoryUUID.toString();
        
        final Path subDirPath = directory.resolve(subDirectoryUUIDString);
    
        if (!subDirPath.toFile().mkdir()) {
            throw new IllegalStateException();
        }

        // Write the change to the changed file
        writeChange(
                change.getChangeReason(),
                subDirectoryUUID,
                (UUIDReasonChangeRefImpl)change.getHistoricRef());
        
        return makeChangeOutputFactory(subDirPath);
    }
    
    @Override
    public void iterateChanges(boolean chronological, ChangeStoreIterator iterator) throws IOException {

        Objects.requireNonNull(iterator);
        
        // Read the changes file
        final ArrayList<UUIDHistoryChangeRefImpl> changeRefs = new ArrayList<>();
        
        readChangeFile(getChangeFile(), (line, parts) -> {
            
            final ChangeReason change = ChangeReason.valueOf(parts[2].toUpperCase());
            
            final UUIDChangeRefImpl historicChangeRef;
            

            switch (parts.length) {
            case 3:
                historicChangeRef = null;
                break;
                
            case 5:
                historicChangeRef = new UUIDChangeRefImpl(
                                UUID.fromString(parts[3]),
                                UUID.fromString(parts[4]));
                break;
                
            default:
                throw new IllegalArgumentException("Unknown line '" + line + "'");
            }

            final UUIDHistoryChangeRefImpl changeRef = new UUIDHistoryChangeRefImpl(
                    UUID.fromString(parts[0]),
                    UUID.fromString(parts[1]),
                    change,
                    historicChangeRef);
            
            changeRefs.add(changeRef);
        });
        
        final int length = changeRefs.size();

        boolean done = false;
        
        if (chronological) {
            for (int i = 0; i < length && !done; ++ i) {
                done = callIterator(iterator, changeRefs.get(i));
            }
        }
        else {
            for (int i = length - 1; i >= 0 && !done; -- i) {
                done = callIterator(iterator, changeRefs.get(i));
            }
        }
    }

    private static boolean callIterator(ChangeStoreIterator iterator, UUIDHistoryChangeRefImpl ref) {
        
        final boolean done;
        
        switch (iterator.onChange(ref, ref.getHistoryChangeRef())) {
        case CONTINUE:
            done = false;
            break;

        case EXIT:
            done = true;
            break;

        default:
            throw new IllegalStateException();
        }
        
        return done;
    }
    
    @Override
    public FileRetrieval createPrevStateChangeInput(ChangeRef changeRef) throws IOException {

        final Path editDir = getPathOfChangeSubDir(changeRef);
        
        return fileStorageFactory.createPrevStateRetrieval(editDir);
    }

    @Override
    public FileRetrieval createEditsChangeInput(ChangeRef changeRef) throws IOException {

        final Path editDir = getPathOfChangeSubDir(changeRef);

        return fileStorageFactory.createEditsRetrieval(editDir);
    }

    @Override
    public void close() throws Exception {
        
        changeFileOutputStream.close();
    }

    private File getChangeFile() {
        
        return basePath.resolve(CHANGE_FILE_NAME).toFile();
    }
    
    private UUID scanLatestChangeUUID(File changeFile) throws IOException {

        final Value<UUID> latest = new Value<>(); 
        
        readChangeFile(changeFile, (line, parts) -> latest.set(UUID.fromString(parts[0])));
        
        return latest.get();
    }
    
    @FunctionalInterface
    interface ChangeEntryProcessor {
        
        void onEntry(String line, String [] parts);
    }

    private void readChangeFile(File changeFile, ChangeEntryProcessor processor) throws IOException {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(changeFile))) {
            
            String line;
            
            while (null != (line = reader.readLine())) {

                final String [] parts = StringUtils.split(line, ' ');
                
                processor.onEntry(line, parts);
            }
        }
    }

    private String makeChangeLine(
            ChangeReason changeReason,
            UUID changeUUID,
            
            // If any history move
            UUID historyTopLevelUUID,
            UUID historyUUID) {
        
        Objects.requireNonNull(currentTopLevelDirectoryUUID);
        Objects.requireNonNull(changeUUID);

        final StringBuilder sb = new StringBuilder(1000);
        
        sb.append(currentTopLevelDirectoryUUID.toString());
        sb.append(' ');
        sb.append(changeUUID.toString());
        sb.append(' ');
        sb.append(changeReason.name().toLowerCase());
        
        if (changeReason.isHistoryMove()) {
            Objects.requireNonNull(historyTopLevelUUID);
            Objects.requireNonNull(historyUUID);
            
            sb.append(' ');
            
            sb.append(historyTopLevelUUID.toString());
            sb.append(' ');
            sb.append(historyUUID.toString());
        }
        else {
            if (historyTopLevelUUID != null) {
                throw new IllegalArgumentException();
            }

            if (historyUUID != null) {
                throw new IllegalArgumentException();
            }
        }

        return sb.toString();
    }
    
    private void writeChange(
            ChangeReason changeReason,
            UUID changeUUID,

            // If any history move
            UUIDReasonChangeRefImpl historyChangeRef) throws IOException {
        
        final UUID historyTopLevelUUID;
        final UUID historyUUID;
        
        if (changeReason.isHistoryMove()) {

            historyTopLevelUUID = historyChangeRef.getTopLevelUUID();
            historyUUID = historyChangeRef.getSubDirUUID();
        }
        else {
            historyTopLevelUUID = null;
            historyUUID = null;
        }
        
        final String changeLine = makeChangeLine(changeReason, changeUUID, historyTopLevelUUID, historyUUID);

        changeFileOutputStream.write(changeLine.getBytes());
        changeFileOutputStream.write('\n');
    }
    
    private static UUID genUUID() {
        
        return UUID.randomUUID();
    }

    private ChangeHistoryStorageFactory makeChangeOutputFactory(Path directory) {
        
        return new ChangeHistoryStorageFactory() {
            
            @Override
            public FileStorage createPrevStateChangeOutput() throws IOException {

                return fileStorageFactory.createPrevStateStorage(directory);
            }

            @Override
            public FileStorage createEditsChangeOutput() throws IOException {

                return fileStorageFactory.createEditsStorage(directory);
            }

            @Override
            public void close() throws Exception {
                
            }
        };
    }

    private Path getPathToTopLevelDirectory(UUID uuid) {

        return basePath.resolve(uuid.toString());
    }

    private File getFileOfTopLevelDirectory(UUID uuid) {
        
        return getPathToTopLevelDirectory(uuid).toFile();
    }

    private Path getPathOfChangeSubDir(ChangeRef changeRef) {
        
        return getPathOfChangeSubDir((UUIDReasonChangeRefImpl)changeRef);
    }

    private Path getPathOfChangeSubDir(UUIDReasonChangeRefImpl changeRef) {
        
        return getPathToTopLevelDirectory(changeRef.getTopLevelUUID())
                                 .resolve(changeRef.getSubDirUUID().toString());
    }
}
