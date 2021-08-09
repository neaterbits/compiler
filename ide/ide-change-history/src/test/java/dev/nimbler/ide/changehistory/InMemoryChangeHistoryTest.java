package dev.nimbler.ide.changehistory;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;

public class InMemoryChangeHistoryTest extends BaseChangeHistoryTest {

    private InMemoryChangeStore changeStore;

    @Before
    public void createChangeStore() {
        
        if (changeStore != null) {
            throw new IllegalStateException();
        }
        
        this.changeStore = new InMemoryChangeStore(false);
    }
    
    @After
    public void resetChangeStore() {
        
        this.changeStore = null;
    }
    
    @Override
    BaseChangeHistory createChangeHistory() throws IOException {

        
        return new InMemoryChangeHistory(ideBasePath(), changeStore);
    }

    @Override
    Path getFullIdePath() {
        return ideBasePath();
    }

    @Override
    boolean storePrevState() {
        return false;
    }
}
