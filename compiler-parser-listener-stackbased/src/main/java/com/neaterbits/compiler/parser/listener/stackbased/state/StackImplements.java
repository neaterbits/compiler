package com.neaterbits.compiler.parser.listener.stackbased.state;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ContextScopedName;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackImplements extends ListStackEntry<ContextScopedName> {

    private final long implementsKeyword;
    private final Context implementsKeywordContext;
    
    public StackImplements(ParseLogger parseLogger, long implementsKeyword, Context implementsKeywordContext) {
        super(parseLogger);
    
        this.implementsKeyword = implementsKeyword;
        this.implementsKeywordContext = implementsKeywordContext;
    }

    public long getImplementsKeyword() {
        return implementsKeyword;
    }

    public Context getImplementsKeywordContext() {
        return implementsKeywordContext;
    }
}

