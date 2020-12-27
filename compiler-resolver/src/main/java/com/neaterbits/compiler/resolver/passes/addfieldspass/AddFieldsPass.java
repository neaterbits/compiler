package com.neaterbits.compiler.resolver.passes.addfieldspass;

import java.util.Objects;

import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.TypeMemberVisitor;
import com.neaterbits.compiler.model.common.passes.MultiPass;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.passes.ParsedModuleAndCodeMap;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class AddFieldsPass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
        extends MultiPass<ParsedModuleAndCodeMap<PARSED_FILE>, ParsedModuleAndCodeMap<PARSED_FILE>> {

    private final CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel;
    
    public AddFieldsPass(CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel) {

        Objects.requireNonNull(compilationUnitModel);

        this.compilationUnitModel = compilationUnitModel;
        
    }

    @Override
    public ParsedModuleAndCodeMap<PARSED_FILE> execute(ParsedModuleAndCodeMap<PARSED_FILE> input) {

        final TypeMemberVisitor fieldVisitor = new AddFieldsVisitor(input.getCodeMap());

        for (ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed : input.getParsed()) {
            iterateClassFields(parsed.getParsedFile(), fieldVisitor);
        }

        return input;
    }
    
    private void iterateClassFields(PARSED_FILE parsedFile, TypeMemberVisitor fieldVisitor) {

        final COMPILATION_UNIT compilationUnit = parsedFile.getCompilationUnit();

        compilationUnitModel.iterateTypesAndMembers(compilationUnit, fieldVisitor, true, false);
    }
}
