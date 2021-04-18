package dev.nimbler.build.buildsystem.maven.common.parse.listeners;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.build.buildsystem.maven.xml.XMLAttribute;

public interface UnknownElementListener {

    void onUnknownTagStart(Context context, String name, List<XMLAttribute> attributes);
    
    void onUnknownTagEnd(Context context, String name);

}
