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
    public final void onIfStatementStart(Context context, long ifKeyword, Context ifKeywordContext) {

        writeStartElementContext(context);

        final int ifKeywordContextRef = writeOtherContext(ifKeywordContext);

        AST.encodeIfElseIfElseStatementStart(astBuffer, ifKeyword, ifKeywordContextRef);
        
        AST.encodeIfConditionBlockStart(astBuffer);
    }

    @Override
    public final void onIfStatementInitialBlockEnd(Context context) {

        AST.encodeIfConditionBlockEnd(astBuffer);
    }

    @Override
    public final void onElseIfStatementStart(Context context, long elseIfKeyword, Context elseIfKeywordContext) {
        
        final int elseIfKeywordContextRef = writeOtherContext(elseIfKeywordContext);

        AST.encodeElseIfConditionBlockStart(astBuffer, elseIfKeyword, elseIfKeywordContextRef);
    }

    @Override
    public final void onElseIfStatementEnd(Context context) {
        
        AST.encodeElseIfConditionBlockEnd(astBuffer);
    }
    
    @Override
    public final void onElseStatementStart(Context context, long elseKeyword, Context elseKeywordContext) {

        final int elseKeywordContextRef = writeOtherContext(elseKeywordContext);
        
        AST.encodeElseBlockStart(astBuffer, elseKeyword, elseKeywordContextRef);
    }

    @Override
    public final void onElseStatementEnd(Context context) {

        AST.encodeElseBlockEnd(astBuffer);
    }

    @Override
    public final void onEndIfStatement(Context context) {

        AST.encodeIfElseIfElseStatementEnd(astBuffer);
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
