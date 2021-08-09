package dev.nimbler.ide.changehistory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;

public class PersistentChangeHistoryTest extends BaseChangeHistoryTest {

    private File tempDir;
    private File storageDir;
    
    @Before
    public void createStorageDirs() throws IOException {
        
        if (tempDir != null) {
            throw new IllegalStateException();
        }
        
        this.tempDir = Files.createTempDirectory("persistenchangehistorytest").toFile();
        
        this.storageDir = new File(tempDir, "storage");
        
        storageDir.mkdirs();

        getFullIdePath().toFile().mkdirs();
    }

    @After
    public void deleteStorageDirs() {
        
        org.jutils.Files.deleteRecursively(tempDir);

        this.tempDir = null;
    }
    
    @Override
    BaseChangeHistory createChangeHistory() throws IOException {

        return new PersistentChangeHistory(storageDir.toPath(), getFullIdePath());
    }

    @Override
    Path getFullIdePath() {

        final File ideDir = new File(tempDir, "testidebasepath");

        final Path ideBasePath = ideBasePath();

        final Path fullIdePath = ideDir.toPath().resolve(ideBasePath);

        return fullIdePath;
    }

    @Override
    boolean storePrevState() {
        return true;
    }
}
