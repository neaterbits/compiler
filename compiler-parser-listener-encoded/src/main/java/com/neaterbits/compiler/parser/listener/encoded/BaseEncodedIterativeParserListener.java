package com.neaterbits.compiler.parser.listener.encoded;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.util.io.strings.Tokenizer;

public abstract class BaseEncodedIterativeParserListener<COMPILATION_UNIT>
    extends BaseInfixParserListener<COMPILATION_UNIT>
    implements IterativeParserListener<COMPILATION_UNIT> {

    protected BaseEncodedIterativeParserListener(String file, Tokenizer tokenizer) {
        super(file, tokenizer);
    }

    @Override
    public final void onIfStatementStart(Context context, String ifKeyword, Context ifKeywordContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIfStatementInitialBlockEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onElseIfStatementStart(Context context, String elseKeyword, Context elseKeywordContext,
            String ifKeyword, Context ifKeywordContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onElseIfStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onElseStatementStart(Context context, String keyword, Context keywordContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onElseStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEndIfStatement(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSwitchStatementStart(Context context, String keyword, Context keywordContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onJavaSwitchBlockStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onJavaSwitchBlockStatementGroupStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSwitchLabelsStart(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSwitchLabelsEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onJavaSwitchBlockStatementGroupEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstantSwitchLabelStart(Context context, String keyword, Context keywordContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstantSwitchLabelEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumSwitchLabel(Context context, String keyword, Context keywordContext, String constantName,
            Context constantNameContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDefaultSwitchLabel(Context context, String keyword, Context keywordContext) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onJavaSwitchBlockEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSwitchStatementEnd(Context context) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onBreakStatement(Context context, String keyword, Context keywordContext, String label) {

        throw new UnsupportedOperationException();
    }
}
