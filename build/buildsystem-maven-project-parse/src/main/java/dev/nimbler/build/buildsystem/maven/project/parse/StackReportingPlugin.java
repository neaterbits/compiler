package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.common.model.configuration.PlexusConfigurationMap;
import dev.nimbler.build.buildsystem.maven.common.parse.StackEntity;
import dev.nimbler.build.buildsystem.maven.common.parse.configuration.ConfigurationSetter;
import dev.nimbler.build.buildsystem.maven.project.model.MavenReportSet;

final class StackReportingPlugin
        extends StackEntity
        implements InheritedSetter, ConfigurationSetter {

    private Boolean inherited;

    private PlexusConfigurationMap configuration;

    private List<MavenReportSet> reportSets;
    
    StackReportingPlugin(Context context) {
        super(context);
    }

    Boolean getInherited() {
        return inherited;
    }

    @Override
    public void setInherited(Boolean inherited) {
        this.inherited = inherited;
    }

    PlexusConfigurationMap getConfiguration() {
        return configuration;
    }

    @Override
    public void setConfiguration(PlexusConfigurationMap configuration) {
        this.configuration = configuration;
    }

    List<MavenReportSet> getReportSets() {
        return reportSets;
    }

    void setReportSets(List<MavenReportSet> reportSets) {
        this.reportSets = reportSets;
    }
}
