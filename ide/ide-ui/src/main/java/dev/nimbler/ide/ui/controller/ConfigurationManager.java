package dev.nimbler.ide.ui.controller;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import javax.xml.bind.JAXB;

import dev.nimbler.build.types.resource.ProjectModuleResourcePath;
import dev.nimbler.ide.component.common.ConfiguredComponent;
import dev.nimbler.ide.component.common.IDERegisteredComponents;

final class ConfigurationManager {

    private final IDERegisteredComponents registeredComponents;
    
    ConfigurationManager(IDERegisteredComponents registeredComponents) {
        
        Objects.requireNonNull(registeredComponents);
        
        this.registeredComponents = registeredComponents;
    }

    private static final String IDE_DIR = ".ide";

    <T> T readComponentConfiguration(
            Class<? extends ConfiguredComponent> componentType,
            Class<?> configurationType,
            ProjectModuleResourcePath module) {
        
        Objects.requireNonNull(componentType);
        Objects.requireNonNull(configurationType);
        Objects.requireNonNull(module);

        final Path path = getConfigurationFilePath(componentType, configurationType, module);
        
        final File file = path.toFile();
        
        final T result;
        
        if (file.exists()) {
            @SuppressWarnings("unchecked")
            final T t = (T)JAXB.unmarshal(file, configurationType);
        
            result = t;
        }
        else {
            result = null;
        }
        
        return result;
    }

    void saveComponentConfiguration(
            Class<? extends ConfiguredComponent> componentType,
            Object configuration,
            ProjectModuleResourcePath module) {

        Objects.requireNonNull(componentType);
        Objects.requireNonNull(configuration);
        Objects.requireNonNull(module);

        final Path path = getConfigurationFilePath(componentType, configuration.getClass(), module);

        final File directory = path.toFile().getParentFile();
        
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        JAXB.marshal(configuration, path.toFile());
    }
    
    private static String getConfigurationFileName(
                List<? extends ConfiguredComponent> configuredComponents,
                Class<?> configurationType) {
        
        String found = null;
        
        for (ConfiguredComponent configuredComponent : configuredComponents) {
            found = configuredComponent.getConfigurationFileName(configurationType);
        
            if (found != null) {
                break;
            }
        }

        return found;
    }
    
    private Path getConfigurationFilePath(
            Class<? extends ConfiguredComponent> componentType,
            Class<?> configurationType,
            ProjectModuleResourcePath module) {
        
        
        final List<? extends ConfiguredComponent> configuredComponents
            = registeredComponents.findComponents(componentType);

        final String configurationFile
            = getConfigurationFileName(configuredComponents, configurationType);
        
        if (configurationFile == null) {
            throw new IllegalStateException();
        }

        final Path path = getProjectConfigDir(module).resolve(configurationFile);
        
        return path;
    }

    private static Path getHomeDir() {
        
        return Path.of(System.getProperty("user.home"));
    }
    
    private static Path getRootConfigDir() {
        
        return getHomeDir().resolve(IDE_DIR);
    }
    
    private static Path getProjectConfigDir(ProjectModuleResourcePath module) {

        if (!module.isAtRoot()) {
            throw new IllegalArgumentException();
        }

        final Path modulePath = module.getFile().toPath().toAbsolutePath();

        final Path homeDir = getHomeDir();
        
        if (!modulePath.startsWith(homeDir)) {
            throw new IllegalArgumentException();
        }

        final Path subtract = homeDir.relativize(modulePath);
        
        return getRootConfigDir().resolve(subtract);
    }
}
