package dev.nimbler.build.buildsystem.maven.plexus.components.parse;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.common.parse.configuration.PlexusConfigurationListener;
import dev.nimbler.build.buildsystem.maven.common.parse.listeners.BaseEventListener;
import dev.nimbler.build.buildsystem.maven.plexus.components.parse.common.PlexusRequirementsListener;

interface PlexusComponentDescriptorEventListener
    extends BaseEventListener,
            PlexusRequirementsListener,
            PlexusConfigurationListener {

    void onComponentSetStart(Context context);
    
    void onComponentsStart(Context context);

    void onComponentStart(Context context);

    void onImplementationStart(Context context);
    
    void onImplementationEnd(Context context);

    void onInstantiationStrategyStart(Context context);
    
    void onInstantiationStrategyEnd(Context context);

    void onComponentEnd(Context context);

    void onComponentsEnd(Context context);
    
    void onComponentSetEnd(Context context);
}
