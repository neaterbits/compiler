package dev.nimbler.build.buildsystem.maven.project.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;

final class StackReports extends StackBase {

    private final List<String> reports;
    
    StackReports(Context context) {
        super(context);
    
        this.reports = new ArrayList<>();
    }

    void add(String report) {

        Objects.requireNonNull(report);

        reports.add(report);
    }
    
    List<String> getReports() {
        return reports;
    }
}
