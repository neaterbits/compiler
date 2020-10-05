package com.neaterbits.compiler.antlr4;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

import com.neaterbits.util.io.strings.OffsetLengthStringRef;
import com.neaterbits.util.parse.context.Context;

public class AntlrStringRefs {

    public static long stringRef(ParserRuleContext ctx) {
        return OffsetLengthStringRef.encode(ctx.start.getStartIndex(), ctx.getText().length());
    }

    public static long stringRef(Context ctx) {
        return OffsetLengthStringRef.encode(ctx.getStartOffset(), ctx.getEndOffset());
    }

    public static long stringRef(Token ctx) {
        return OffsetLengthStringRef.encode(ctx.getStartIndex(), ctx.getText().length());
    }

    public static long stringRef(TerminalNode ctx) {
        return stringRef(ctx.getSymbol());
    }
}
