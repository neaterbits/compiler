package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.Objects;
import java.util.Properties;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;

final class StackCiConfiguration extends StackBase {

    private final Properties properties;
    
    StackCiConfiguration(Context context) {
        super(context);
        
        this.properties = new Properties();
    }

    void addKeyValue(String key, String value) {
        
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        
        properties.put(key, value);
    }

    Properties getProperties() {
        return properties;
    }
}
