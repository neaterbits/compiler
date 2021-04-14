package com.neaterbits.compiler.parser.listener.encoded;

import com.neaterbits.compiler.ast.encoded.AST;
import com.neaterbits.compiler.parser.listener.common.InfixParseTreeListener;
import com.neaterbits.compiler.types.operator.IncrementDecrement;
import com.neaterbits.compiler.types.operator.Operator;
import com.neaterbits.util.io.strings.Tokenizer;
import com.neaterbits.util.parse.context.Context;

abstract class BaseInfixParserListener<COMPILATION_UNIT>
    extends BaseParserListener<COMPILATION_UNIT> implements InfixParseTreeListener<COMPILATION_UNIT> {

    BaseInfixParserListener(String file, Tokenizer tokenizer) {
        super(file, tokenizer);
    }

    
    @Override
    public void onUnaryExpressionStart(int startContext, Operator operator) {

        writeStartElementContextRef(startContext);
        
        AST.encodeUnaryExpressionStart(astBuffer, operator);
    }


    @Override
    public void onUnaryExpressionEnd(int startContext, Context endContext) {
        
        writeEndElementContext(startContext, endContext);
        
        AST.encodeUnaryExpressionEnd(astBuffer);
    }

    @Override
    public final void onExpressionBinaryOperator(int leafContext, Operator operator) {

        writeLeafElementContextRef(leafContext);
        
        AST.encodeExpressionBinaryOperator(astBuffer, operator);
    }

    @Override
    public final void onIncrementDecrementExpressionStart(
            int incrementDecrementExpressionStartContext,
            IncrementDecrement operator) {
        
        writeStartElementContextRef(incrementDecrementExpressionStartContext);

        throw new UnsupportedOperationException();
    }

    @Override
    public final void onIncrementDecrementExpressionEnd(int incrementDecrementExpressionStartContext, Context endContext) {

        writeEndElementContext(incrementDecrementExpressionStartContext, endContext);
        
        throw new UnsupportedOperationException();
    }
}
