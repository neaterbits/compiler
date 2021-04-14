package dev.nimbler.compiler.parser.java.recursive;

import java.util.Map;

import com.neaterbits.util.buffers.MapStringStorageBuffer;
import com.neaterbits.util.io.strings.Tokenizer;

import dev.nimbler.compiler.ast.encoded.ASTBuffer;
import dev.nimbler.compiler.ast.encoded.ASTBufferRead;
import dev.nimbler.compiler.ast.encoded.EncodedCompilationUnit;
import dev.nimbler.compiler.parser.listener.encoded.BaseEncodedIterativeParserListener;
import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.IntKeyIntValueHash;
import dev.nimbler.language.common.types.TypeName;


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
