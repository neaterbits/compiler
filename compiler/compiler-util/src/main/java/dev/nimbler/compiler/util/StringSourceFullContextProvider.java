package dev.nimbler.compiler.util;

import java.util.Objects;

import org.jutils.io.strings.StringSource;
import org.jutils.parse.context.Context;
import org.jutils.parse.context.FullContext;
import org.jutils.parse.context.ImmutableFullContext;

public final class StringSourceFullContextProvider implements FullContextProvider {
    
    private final String file;
    private final StringSource stringSource;

    
    public StringSourceFullContextProvider(String file, StringSource stringSource) {
        
        Objects.requireNonNull(stringSource);
        
        this.file = file;
        this.stringSource = stringSource;
    }

    @Override
    public FullContext makeFullContext(Context context) {
        return new ImmutableFullContext(file, -1, -1, context.getStartOffset(), -1, -1, context.getEndOffset(), getText(context));
    }

    @Override
    public String getText(Context context) {
        return stringSource.asStringFromOffset(context.getStartOffset(), context.getEndOffset());
    }
}
