package dev.nimbler.compiler.parser.listener.stackbased.state;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.ListStackEntry;
import dev.nimbler.compiler.util.ContextScopedName;
import dev.nimbler.compiler.util.parse.ParseLogger;

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

