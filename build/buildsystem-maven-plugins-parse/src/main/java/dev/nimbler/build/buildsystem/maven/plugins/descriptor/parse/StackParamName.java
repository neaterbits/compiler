package dev.nimbler.build.buildsystem.maven.plugins.descriptor.parse;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.plugins.descriptor.model.MojoConfiguration;

final class StackParamName extends StackBase {

    private final String paramName;

    private String implementation;
    
    private String defaultValue;
    
    StackParamName(Context context, String paramName) {
        super(context);
        
        Objects.requireNonNull(paramName);
        
        this.paramName = paramName;
    }

    void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    MojoConfiguration build() {
        return new MojoConfiguration(paramName, implementation, defaultValue);
    }
    
}
