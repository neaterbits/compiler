package dev.nimbler.build.buildsystem.maven.common.parse.configuration;

import org.jutils.parse.context.Context;

public interface PlexusConfigurationListener {

    void onConfigurationStart(Context context);
    
    void onConfigurationEnd(Context context);

}
