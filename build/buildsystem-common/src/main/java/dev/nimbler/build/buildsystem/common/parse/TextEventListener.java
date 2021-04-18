package dev.nimbler.build.buildsystem.common.parse;

import org.jutils.parse.context.Context;

public interface TextEventListener {

    void onText(Context context, String text);

}
