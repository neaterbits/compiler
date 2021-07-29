package dev.nimbler.ide.component.common;

public interface ConfiguredComponent extends IDEComponent {

    String getConfigurationFileName(Class<?> configurationType);
}
