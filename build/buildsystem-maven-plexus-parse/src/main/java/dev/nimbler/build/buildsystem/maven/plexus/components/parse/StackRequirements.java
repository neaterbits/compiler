package dev.nimbler.build.buildsystem.maven.plexus.components.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.plexus.components.model.PlexusRequirement;

final class StackRequirements extends StackBase {

    private final List<PlexusRequirement> requirements;

    StackRequirements(Context context) {
        super(context);

        this.requirements = new ArrayList<>();
    }

    void add(PlexusRequirement requirement) {
        
        Objects.requireNonNull(requirement);
    
        requirements.add(requirement);
    }

    List<PlexusRequirement> getRequirements() {
        return requirements;
    }
}
