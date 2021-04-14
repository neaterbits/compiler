package dev.nimbler.compiler.parser.listener.common;

import com.neaterbits.util.parse.context.Context;

public interface ContextReadAccess {

    Context getContext(int index);
}
