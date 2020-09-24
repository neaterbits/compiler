package com.neaterbits.compiler.model.encoded;

import java.util.Map;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.parser.listener.encoded.ASTBufferRead;
import com.neaterbits.compiler.parser.listener.encoded.BaseEncodedIterativeParserListener;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.IntKeyIntValueHash;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.Tokenizer;

public final class EncodedParserListener
    extends BaseEncodedIterativeParserListener<EncodedCompilationUnit> {

    public EncodedParserListener(String file, Tokenizer tokenizer) {
        super(file, tokenizer);
    }

    @Override
    protected EncodedCompilationUnit makeCompilationUnit(
            ASTBufferRead astBuffer,
            ASTBufferRead contextBuffer,
            FullContextProvider fullContextProvider,
            IntKeyIntValueHash parseTreeRefToStartContextHash,
            IntKeyIntValueHash parseTreeRefToEndContextHash,
            Map<TypeName, Integer> typeNameToIndex,
            MapStringStorageBuffer stringBuffer) {

        return new EncodedCompilationUnit(
                astBuffer,
                contextBuffer,
                fullContextProvider,
                parseTreeRefToStartContextHash,
                parseTreeRefToEndContextHash,
                typeNameToIndex,
                stringBuffer,
                true);
    }
}
