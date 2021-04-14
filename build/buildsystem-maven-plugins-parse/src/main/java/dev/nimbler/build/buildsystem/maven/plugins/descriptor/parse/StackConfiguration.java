package dev.nimbler.build.buildsystem.maven.plugins.descriptor.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.plugins.descriptor.model.MojoConfiguration;

final class StackConfiguration extends StackBase {

    private List<MojoConfiguration> params;
    
    StackConfiguration(Context context) {
        super(context);
    }
    
    void addParam(MojoConfiguration param) {
        
        Objects.requireNonNull(param);
    
        if (params == null) {
            this.params = new ArrayList<>();
        }
        
        params.add(param);
    }

    List<MojoConfiguration> getParams() {
        return params;
    }
}
