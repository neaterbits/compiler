package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.project.model.MavenBuildPlugin;

final class StackPluginManagement extends StackBase implements PluginsSetter {

    private List<MavenBuildPlugin> plugins;
    
    StackPluginManagement(Context context) {
        super(context);
    }

    List<MavenBuildPlugin> getPlugins() {
        return plugins;
    }

    @Override
    public void setPlugins(List<MavenBuildPlugin> plugins) {
        this.plugins = plugins;
    }
}
