package dev.nimbler.build.buildsystem.maven.project.parse;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.common.model.configuration.MavenConfiguration;
import dev.nimbler.build.buildsystem.maven.common.parse.configuration.ConfigurationSetter;
import dev.nimbler.build.buildsystem.maven.common.parse.configuration.StackBaseConfigurable;

abstract class StackConfigurable
        extends StackBaseConfigurable
        implements InheritedSetter, ConfigurationSetter {

    private Boolean inherited;

    StackConfigurable(Context context) {
        super(context);
    }

    final Boolean getInherited() {
        return inherited;
    }

    @Override
    public final void setInherited(Boolean inherited) {
        this.inherited = inherited;
    }

    final MavenConfiguration makeMavenConfiguration() {
        return new MavenConfiguration(inherited, getConfiguration());
    }
}
