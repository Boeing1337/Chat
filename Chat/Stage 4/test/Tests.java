import chat.client.Client;
import chat.server.Server;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import static org.hyperskill.hstest.common.Utils.sleep;

public class Tests extends StageTest<String> {

    @DynamicTestingMethod
    CheckResult test() {
        final TestedProgram server = new TestedProgram(Server.class);
        final TestedProgram client1 = new TestedProgram(Client.class);
        final TestedProgram client2 = new TestedProgram(Client.class);
        final TestedProgram client3 = new TestedProgram(Client.class);
        final int appsStartPause = 50;

        server.startInBackground();
        client1.start();
        client2.start();
        sleep(appsStartPause);

        String a = client1.execute("Hello!");
        sleep(appsStartPause);
        String b = client2.execute("2!");
        sleep(appsStartPause);
        // System.out.println(client1.execute("Hello!"));
        sleep(appsStartPause);
        sleep(appsStartPause);
        sleep(appsStartPause);
        sleep(appsStartPause);

        return CheckResult.correct();
    }
}