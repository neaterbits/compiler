package dev.nimbler.build.buildsystem.maven.common.parse.configuration;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.common.model.configuration.PlexusConfigurationMap;

final class StackPlexusConfigurationMap extends StackConfigurationLevel {

    StackPlexusConfigurationMap(Context context) {
        super(context, "configuration");
    }

    PlexusConfigurationMap getConfiguration() {
        return getObject();
    }
}
