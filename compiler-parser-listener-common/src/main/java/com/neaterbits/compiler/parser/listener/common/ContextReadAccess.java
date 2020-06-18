package com.neaterbits.compiler.parser.listener.common;

import com.neaterbits.compiler.util.Context;

public interface ContextReadAccess {

    Context getContext(int index);
}
