package dev.nimbler.ide.component.runners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.runners.model.RunConfiguration;
import dev.nimbler.ide.component.runners.model.RunnersConfiguration;

public class RunnersConfigurationHelper {

    public static RunnersConfiguration readAndMergeRunnerConfiguration(ComponentIDEAccess componentIDEAccess) throws IOException {

        final List<RunConfiguration> runConfigurations = new ArrayList<>();
        
        for (ProjectModuleResourcePath path : componentIDEAccess.getRootModules()) {
            
            final RunnersConfiguration runnersConfiguration
                    = componentIDEAccess.readConfigurationFile(
                                                RunnersComponent.class,
                                                RunnersConfiguration.class,
                                                path);

            if (runnersConfiguration != null && runnersConfiguration.getRunConfigurations() != null) {
                runConfigurations.addAll(runnersConfiguration.getRunConfigurations());
            }
        }
        
        final RunnersConfiguration result = new RunnersConfiguration();
        
        result.setRunConfigurations(runConfigurations);
        
        return result;
    }
    
    public static void splitAndSaveRunnerConfiguration(
            ComponentIDEAccess componentIDEAccess,
            List<RunConfiguration> runConfigurations) throws IOException {
        
        final Set<ProjectModuleResourcePath> distinctResourcePaths
            = runConfigurations.stream()
                    .map(RunConfiguration::getProject)
                    .map(module -> module.getRoot())
                    .collect(Collectors.toSet());
                    
        for (ProjectModuleResourcePath resourcePath : distinctResourcePaths) {
    
            final RunnersConfiguration runnersConfiguration
                = new RunnersConfiguration();
            
            final List<RunConfiguration> configs = runConfigurations.stream()
                    .filter(config -> config.getProject().getRoot().equals(resourcePath))
                    .collect(Collectors.toList());

            runnersConfiguration.setRunConfigurations(configs);
            
            componentIDEAccess.saveConfigurationFile(
                    RunnersComponent.class,
                    runnersConfiguration,
                    resourcePath);
        }
    }
}
