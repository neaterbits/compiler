package dev.nimbler.ide.changehistory;

abstract class InMemoryCloseable implements AutoCloseable {

    private volatile boolean closed;

    final void checkClosed() {

        if (closed) {
            throw new IllegalStateException();
        }
    }

    @Override
    public void close() throws Exception {
        
        checkClosed();
        
        this.closed = true;
    }
}
