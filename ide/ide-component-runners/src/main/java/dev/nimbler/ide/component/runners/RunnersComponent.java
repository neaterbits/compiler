package dev.nimbler.ide.component.runners;

import dev.nimbler.ide.component.common.ConfiguredComponent;
import dev.nimbler.ide.component.runners.model.RunnersConfiguration;

public class RunnersComponent implements ConfiguredComponent {

    @Override
    public String getConfigurationFileName(Class<?> configurationType) {

        final String fileName;
        
        if (configurationType.equals(RunnersConfiguration.class)) {
            fileName = "runners";
        }
        else {
            fileName = null;
        }

        return fileName;
    }
}
