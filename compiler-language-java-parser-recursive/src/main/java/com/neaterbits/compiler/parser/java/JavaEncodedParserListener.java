package com.neaterbits.compiler.parser.java;

import java.util.Map;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.ast.encoded.ASTBuffer;
import com.neaterbits.compiler.ast.encoded.ASTBufferRead;
import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.parser.listener.encoded.BaseEncodedIterativeParserListener;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.IntKeyIntValueHash;
import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.Tokenizer;


public class JavaEncodedParserListener
    extends BaseEncodedIterativeParserListener<EncodedCompilationUnit> {

    public JavaEncodedParserListener(String file, Tokenizer tokenizer) {
        super(file, tokenizer);
    }

    @Override
    protected EncodedCompilationUnit makeCompilationUnit(
            ASTBuffer astBuffer,
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
