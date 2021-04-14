CLASSPATH=structured-log-model/target/structured-log-model-0.0.1-SNAPSHOT.jar:structured-log-swt/target/structured-log-swt-0.0.1-SNAPSHOT.jar:util/target/util-0.0.1-SNAPSHOT.jar:util-swt/target/util-swt-0.0.1-SNAPSHOT.jar:~/.m2/repository/org/eclipse/swt/org.eclipse.swt.gtk.linux.x86_64/v20181204-1801/org.eclipse.swt.gtk.linux.x86_64-v20181204-1801.jar:~/.m2/repository/org/eclipse/jface/org.eclipse.jface/3.8.0.v20120521-2329/org.eclipse.jface-3.8.0.v20120521-2329.jar:~/.m2/repository/org/eclipse/equinox/org.eclipse.equinox.common/3.6.100.v20120522-1841/org.eclipse.equinox.common-3.6.100.v20120522-1841.jar:~/.m2/repository/org/eclipse/core/org.eclipse.core.commands/3.6.1.v20120521-2329/org.eclipse.core.commands-3.6.1.v20120521-2329.jar:~/.m2/repository/org/eclipse/osgi/org.eclipse.osgi/3.8.0.v20120529-1548/org.eclipse.osgi-3.8.0.v20120529-1548.jar

args=$@

java -cp $CLASSPATH com.neaterbits.structuredlog.swt.StructuredLogMain $args


