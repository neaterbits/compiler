package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.List;

import org.jutils.parse.context.Context;

final class StackReportSet extends StackConfigurable implements IdSetter {

    private String id;

    private List<String> reports;
    
    StackReportSet(Context context) {
        super(context);
    }

    String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    void setReports(List<String> reports) {
        this.reports = reports;
    }

    List<String> getReports() {
        return reports;
    }
}
