package com.neaterbits.compiler.util;

import java.util.Objects;

import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.parse.context.Context;
import com.neaterbits.util.parse.context.FullContext;
import com.neaterbits.util.parse.context.ImmutableFullContext;

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
