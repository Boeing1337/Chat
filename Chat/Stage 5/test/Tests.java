import chat.client.Client;
import chat.server.Server;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import static org.hyperskill.hstest.common.Utils.sleep;

public class Tests extends StageTest<String> {
    private String space = "\n\n";

    @DynamicTestingMethod
    CheckResult test() {
        final TestedProgram server = new TestedProgram(Server.class);
        final TestedProgram client1 = new TestedProgram(Client.class);
        final TestedProgram client2 = new TestedProgram(Client.class);
        final TestedProgram client3 = new TestedProgram(Client.class);
        client1.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        client3.setReturnOutputAfterExecution(false);

        server.startInBackground();
        client1.start();
        client2.start();

        client1.execute("/auth asdasd asdasd");
        client2.execute("/registration login passssss");
        client2.execute("/exit");

        sleep(50);
        client1.execute("/auth login passssss");
        sleep(50);
        System.out.println(space + client1.getOutput().trim() + space);
        client1.execute("/list");
        sleep(50);
        System.out.println(space + client1.getOutput().trim() + space);


        return CheckResult.correct();
    }
}