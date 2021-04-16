package dev.nimbler.ide.common.ui.actions;

import com.neaterbits.util.threads.ForwardResultToCaller;

import dev.nimbler.build.model.BuildRoot;

public interface ActionExeParameters extends ActionSourceFileParameters {

    BuildRoot getBuildRoot();

    ForwardResultToCaller getForwardResultToCaller();
}
