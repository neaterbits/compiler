CLASSPATH=build-common/target/build-common-0.0.1-SNAPSHOT.jar:buildsystem-common/target/buildsystem-common-0.0.1-SNAPSHOT.jar:buildsystem-maven/target/buildsystem-maven-0.0.1-SNAPSHOT.jar:util/target/util-0.0.1-SNAPSHOT.jar:structured-log-model/target/structured-log-model-0.0.1-SNAPSHOT.jar:bytecode-common/target/bytecode-common-0.0.1-SNAPSHOT.jar:build-main/target/build-main-0.0.1-SNAPSHOT.jar:component-common/target/component-common-0.0.1-SNAPSHOT.jar:build-language-java-jdk/target/build-language-java-jdk-0.0.1-SNAPSHOT.jar:~/projects/compiler/compiler-util/target/compiler-util-0.0.1-SNAPSHOT.jar:~/projects/compiler/bytecode-common/target/bytecode-common-0.0.1-SNAPSHOT.jar:~/projects/compiler/bytecode-java/target/bytecode-java-0.0.1-SNAPSHOT.jar

args=$@

java -cp $CLASSPATH com.neaterbits.ide.buildmain.BuildMain $args


