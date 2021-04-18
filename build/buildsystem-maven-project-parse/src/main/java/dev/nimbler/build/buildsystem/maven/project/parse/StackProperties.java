package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;

final class StackProperties extends StackBase {

    private final Map<String, String> properties;
    
    StackProperties(Context context) {
        super(context);
        
        this.properties = new HashMap<>();
    }
    
    
    void add(String name, String value) {
        
        Objects.requireNonNull(name);
        
        properties.put(name, value);
    }


    Map<String, String> getProperties() {
        return properties;
    }
}
