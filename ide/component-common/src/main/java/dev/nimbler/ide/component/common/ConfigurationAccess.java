package dev.nimbler.ide.component.common;

import dev.nimbler.ide.common.config.Configuration;

public interface ConfigurationAccess {

    <T extends Configuration> T getConfiguration(Class<T> type);
}
