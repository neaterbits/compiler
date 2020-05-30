package com.neaterbits.compiler.parser.java;

import java.util.Map;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.parser.listener.encoded.ASTBufferRead;
import com.neaterbits.compiler.parser.listener.encoded.BaseEncodedIterativeParserListener;
import com.neaterbits.compiler.util.IntKeyIntValueHash;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.Tokenizer;


public class JavaEncodedParserListener
    extends BaseEncodedIterativeParserListener<EncodedCompilationUnit> {

    public JavaEncodedParserListener(String file, Tokenizer tokenizer) {
        super(file, tokenizer);
    }

    @Override
    protected EncodedCompilationUnit makeCompilationUnit(
            String file,
            ASTBufferRead astBuffer,
            ASTBufferRead contextBuffer,
            IntKeyIntValueHash parseTreeRefToStartContextHash,
            IntKeyIntValueHash parseTreeRefToEndContextHash,
            Map<TypeName, Integer> typeNameToIndex,
            MapStringStorageBuffer stringBuffer) {

        return new EncodedCompilationUnit(
                file,
                astBuffer,
                contextBuffer,
                parseTreeRefToStartContextHash,
                parseTreeRefToEndContextHash,
                typeNameToIndex,
                stringBuffer,
                true);
    }
}
