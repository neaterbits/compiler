package dev.nimbler.compiler.parser.listener.stackbased.state.setters;

import java.util.List;

public interface NamedGenericParametersSetter<NAMED_GENERIC_TYPE_PARAMETERS> {

    void setGenericTypes(List<NAMED_GENERIC_TYPE_PARAMETERS> genericTypes);
}
