package dev.nimbler.ide.component.runner.test;

import java.util.Objects;

final class OutputBuffer {

    private final StringBuilder sb;
    
    OutputBuffer() {
        this.sb = new StringBuilder();
    }

    void reset() {
        sb.setLength(0);
    }
    
    void add(String output) {
        Objects.requireNonNull(output);
        
        sb.append(output);
    }
    
    String retrieveLines() {
        
        int newlineIndex = findPriorNewlineOrStartOfBuffer(sb);

        if (newlineIndex > 0) {
            newlineIndex = skipEmptyLines(sb, newlineIndex);
        }

        final String lines;

        // Do not return if starts with empty line since may be prefix for '#TESTLINE' or '#TESTBLOCK'
        // and should be discarded, not sent on to stdout
        if (newlineIndex > 0) {
            
            final int afterNewlineIndex;
            
            if (sb.charAt(newlineIndex) == '\n') {
                afterNewlineIndex = newlineIndex + 1;
            }
            else if (    newlineIndex + 1 < sb.length()
                      && sb.charAt(newlineIndex) == '\r'
                      && sb.charAt(newlineIndex + 1) == '\n') {

                afterNewlineIndex = newlineIndex + 2;
            }
            else {
                throw new IllegalStateException();
            }

            lines = sb.substring(0, afterNewlineIndex);
            sb.delete(0, afterNewlineIndex);
        }
        else {
            lines = null;
        }
        
        return lines;
    }
    
    private static int findPriorNewlineOrStartOfBuffer(StringBuilder sb) {

        int priorOrStart = 0;
        
        for (int i = sb.length() - 1; i >= 0; -- i) {
            
            if (sb.charAt(i) == '\n') {
                
                if (i > 0 && sb.charAt(i - 1) == '\r') {
                    priorOrStart = i - 1;
                }
                else {
                    priorOrStart = i;
                }
                break;
            }
        }
        
        return priorOrStart;
    }
    
    private static int skipEmptyLines(StringBuilder sb, int i) {
        
        if (i >= sb.length()) {
            throw new IllegalArgumentException();
        }

        if (i <= 0) {
            throw new IllegalArgumentException();
        }

        int afterLastNonEmptyLine = -1;
        
        int pos = i;
        
        for (;;) {
            
            final char c = sb.charAt(pos);
            
            if (c != '\n' && c != '\r') {
                afterLastNonEmptyLine = pos + 1;
                break;
            }
            
            if (pos == 0) {
                afterLastNonEmptyLine = 0;
                break;
            }
            else {
                -- pos;
            }
        }
        
        return afterLastNonEmptyLine;
    }
}
