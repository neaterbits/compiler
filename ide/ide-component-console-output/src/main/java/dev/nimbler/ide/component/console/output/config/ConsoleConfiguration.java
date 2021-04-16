package dev.nimbler.ide.component.console.output.config;

import java.util.Objects;

import dev.nimbler.ide.common.config.Configuration;
import dev.nimbler.ide.util.ui.text.LineDelimiter;

public final class ConsoleConfiguration extends Configuration {

    private final LineDelimiter lineDelimiter;
    
    private final int maxDisplayed;

    public ConsoleConfiguration(LineDelimiter lineDelimiter, int maxDisplayed) {
        
        Objects.requireNonNull(lineDelimiter);
        
        this.lineDelimiter = lineDelimiter;
        this.maxDisplayed = maxDisplayed;
    }

    public LineDelimiter getLineDelimiter() {
        return lineDelimiter;
    }

    public int getMaxDisplayed() {
        return maxDisplayed;
    }
}
