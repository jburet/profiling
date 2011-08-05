package fr.xebia.profiling.test.manual;

import fr.xebia.profiling.test.manual.tested.TestedApp;

/**
 * launch with
 * -javaagent:logging-agent/target/logging-agent-0.1-SNAPSHOT.jar
 * and set env var
 * LoggingAgent.configuration.path=logging-agent/src/test/conf/manual-test.conf
 */
public class ApplicationTest {

    public static void main(String... args) {
        TestedApp app = new TestedApp();
        app.method1();
        app.method2(10000);
        app.method3(10000, 10);
    }

}
