package dev.nimbler.build.buildsystem.maven.plexus.components.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.plexus.components.model.PlexusComponent;

final class StackComponents extends StackBase {

    private final List<PlexusComponent> components;

    public StackComponents(Context context) {
        super(context);
        
        this.components = new ArrayList<>();
    }
    
    void add(PlexusComponent component) {
        
        Objects.requireNonNull(component);
        
        components.add(component);
    }

    List<PlexusComponent> getComponents() {
        return components;
    }
}
