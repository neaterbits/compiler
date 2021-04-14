package dev.nimbler.build.buildsystem.maven.common.parse.configuration;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.common.model.configuration.PlexusConfiguration;
import dev.nimbler.build.buildsystem.maven.common.model.configuration.PlexusConfigurationMap;

public abstract class StackBaseConfigurable extends StackBase implements ConfigurationSetter {

    private PlexusConfigurationMap configurationMap;

    protected StackBaseConfigurable(Context context) {
        super(context);
    }

    protected final PlexusConfigurationMap getConfiguration() {
        return configurationMap;
    }

    @Override
    public final void setConfiguration(PlexusConfigurationMap configuration) {
        this.configurationMap = configuration;
    }

    public final PlexusConfiguration makePlexusConfiguration() {
        return new PlexusConfiguration(getConfiguration());
    }
}
