package dev.nimbler.build.buildsystem.maven.project.parse;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.build.buildsystem.common.parse.StackBase;

final class StackFiles extends StackBase {

    private Boolean enabled;
    private String updatePolicy;
    private String checksumPolicy;
    private String layout;
    
    StackFiles(Context context) {
        super(context);
    }

    Boolean getEnabled() {
        return enabled;
    }

    void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    String getUpdatePolicy() {
        return updatePolicy;
    }

    void setUpdatePolicy(String updatePolicy) {
        this.updatePolicy = updatePolicy;
    }

    String getChecksumPolicy() {
        return checksumPolicy;
    }

    void setChecksumPolicy(String checksumPolicy) {
        this.checksumPolicy = checksumPolicy;
    }

    String getLayout() {
        return layout;
    }

    void setLayout(String layout) {
        this.layout = layout;
    }
}
