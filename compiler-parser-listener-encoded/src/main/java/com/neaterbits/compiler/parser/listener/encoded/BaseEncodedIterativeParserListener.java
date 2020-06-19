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
    public final void onIfStatementStart(int ifStatementStartContext, long ifKeyword, int ifKeywordContext) {

        // whole if - else if - else statement
        writeStartElementContextRef(ifStatementStartContext);
        AST.encodeIfElseIfElseStatementStart(astBuffer, ifKeyword, ifKeywordContext);
    }

    @Override
    public void onIfStatementInitialBlockStart(int ifStatementInitialBlockStartContext) {

        writeStartElementContextRef(ifStatementInitialBlockStartContext);
        AST.encodeIfConditionBlockStart(astBuffer);
    }

    @Override
    public final void onIfStatementInitialBlockEnd(int ifStatementInitialBlockStartContext, Context endContext) {

        writeEndElementContext(ifStatementInitialBlockStartContext, endContext);
        
        AST.encodeIfConditionBlockEnd(astBuffer);
    }

    @Override
    public final void onElseIfStatementStart(
            int elseIfStatementStartContext,
            long elseKeyword, int elseKeywordContext,
            long ifKeyword, int ifKeywordContext) {
        
        verifyNotSameContext(elseIfStatementStartContext, elseKeywordContext, ifKeywordContext);
        
        writeStartElementContextRef(elseIfStatementStartContext);
        
        AST.encodeElseIfConditionBlockStart(
                astBuffer,
                elseKeyword, elseKeywordContext,
                ifKeyword, ifKeywordContext);
    }

    @Override
    public final void onElseIfStatementEnd(int elseIfStatementStartContext, Context endContext) {
        
        writeEndElementContext(elseIfStatementStartContext, endContext);
        
        AST.encodeElseIfConditionBlockEnd(astBuffer);
    }
    
    @Override
    public final void onElseStatementStart(int elseStatementStartContext, long elseKeyword, int elseKeywordContext) {
        
        verifyNotSameContext(elseStatementStartContext, elseKeywordContext);

        writeStartElementContextRef(elseStatementStartContext);
        
        AST.encodeElseBlockStart(astBuffer, elseKeyword, elseKeywordContext);
    }

    @Override
    public final void onElseStatementEnd(int elseStatementStartContext, Context endContext) {

        writeEndElementContext(elseStatementStartContext, endContext);
        
        AST.encodeElseBlockEnd(astBuffer);
    }

    @Override
    public final void onEndIfStatement(int ifStatementStartContext, Context endContext) {

        writeEndElementContext(ifStatementStartContext, endContext);
        
        AST.encodeIfElseIfElseStatementEnd(astBuffer);
    }

    @Override
    public final void onSwitchStatementStart(int switchStatementStartContext, long keyword, int keywordContext) {
        
        verifyNotSameContext(switchStatementStartContext, keywordContext);
        
        writeStartElementContextRef(switchStatementStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onJavaSwitchBlockStart(int javaSwitchBlockStartContext) {

        writeStartElementContextRef(javaSwitchBlockStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onJavaSwitchBlockStatementGroupStart(int javaSwitchBlockStatementGroupStartContext) {

        writeStartElementContextRef(javaSwitchBlockStatementGroupStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSwitchLabelsStart(int switchLabelsStartContext) {

        writeStartElementContextRef(switchLabelsStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSwitchLabelsEnd(int switchLabelsStartContext, Context endContext) {

        writeEndElementContext(switchLabelsStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onJavaSwitchBlockStatementGroupEnd(int javaSwitchBlockStatementGroupStartContext, Context endContext) {

        writeEndElementContext(javaSwitchBlockStatementGroupStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstantSwitchLabelStart(int constantSwitchLabelStartContext, long keyword, int keywordContext) {
        
        verifyNotSameContext(constantSwitchLabelStartContext, keywordContext);

        writeStartElementContextRef(constantSwitchLabelStartContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onConstantSwitchLabelEnd(int constantSwitchLabelStartContext, Context endContext) {
        
        writeEndElementContext(constantSwitchLabelStartContext, endContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onEnumSwitchLabel(int enumSwitchLabelStartContext, long keyword, int keywordContext, long constantName,
            int constantNameContext) {
        
        verifyNotSameContext(enumSwitchLabelStartContext, keywordContext, constantNameContext);
        
        writeStartElementContextRef(enumSwitchLabelStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onDefaultSwitchLabel(int leafContext, long keyword) {

        writeLeafElementContextRef(leafContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onJavaSwitchBlockEnd(int javaSwitchBlockStartContext, Context endContext) {

        writeEndElementContext(javaSwitchBlockStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onSwitchStatementEnd(int switchStatementStartContext, Context endContext) {

        writeEndElementContext(switchStatementStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }

    @Override
    public final void onBreakStatement(
            int breakStatementStartContext,
            long keyword,
            int keywordContext,
            long label,
            int labelContext,
            Context endContext) {

        writeStartElementContextRef(breakStatementStartContext);
        
        if (Boolean.TRUE) {
            throw new UnsupportedOperationException();
        }

        writeEndElementContext(breakStatementStartContext, endContext);
    }
}
