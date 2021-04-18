package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;
import dev.nimbler.build.buildsystem.maven.project.model.MavenReportSet;

final class StackReportSets extends StackBase {

    private final List<MavenReportSet> reportSets;
    
    StackReportSets(Context context) {
        super(context);

        this.reportSets = new ArrayList<>();
    }

    void add(MavenReportSet reportSet) {
        
        Objects.requireNonNull(reportSet);

        reportSets.add(reportSet);
    }
    
    List<MavenReportSet> getReportSets() {
        return reportSets;
    }
}
