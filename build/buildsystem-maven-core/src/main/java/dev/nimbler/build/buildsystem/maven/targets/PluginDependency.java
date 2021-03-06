package dev.nimbler.build.buildsystem.maven.targets;

import java.util.List;
import java.util.stream.Collectors;

import dev.nimbler.build.buildsystem.maven.project.model.MavenPlugin;
import dev.nimbler.build.buildsystem.maven.project.model.MavenPluginRepository;
import dev.nimbler.build.buildsystem.maven.project.model.MavenProject;

final class PluginDependency extends TransitiveDependency {

    private final MavenPlugin plugin;

    PluginDependency(
            MavenProject originalDependency,
            List<MavenPluginRepository> referencedFromRepositories,
            MavenPlugin plugin) {
        
        super(
                null,
                originalDependency,
                referencedFromRepositories.stream().collect(Collectors.toList()),
                plugin.getModuleId());
        
        this.plugin = plugin;
    }

    MavenPlugin getPlugin() {
        return plugin;
    }

    @Override
    String getTargetedDependencyClassifier() {
        return null;
    }
}
