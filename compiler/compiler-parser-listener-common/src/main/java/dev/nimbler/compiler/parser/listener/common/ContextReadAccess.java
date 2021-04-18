package dev.nimbler.compiler.parser.listener.common;

import org.jutils.parse.context.Context;

public interface ContextReadAccess {

    Context getContext(int index);
}
