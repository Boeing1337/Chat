import chat.client.Client;
import chat.server.Server;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import static org.hyperskill.hstest.common.Utils.sleep;

public class Tests extends StageTest<String> {
    private final int executePause = 25;

    @DynamicTestingMethod
    CheckResult test1() {
        final TestedProgram server1 = new TestedProgram(Server.class);
        final TestedProgram client1 = new TestedProgram(Client.class);
        client1.setReturnOutputAfterExecution(false);
        final String fromClientMsg = "You are in my mind!";

        server1.startInBackground();
        client1.start();
        sleep(executePause);

        if (!client1.getOutput().trim().equals("Client started!"))
            return CheckResult.wrong("Can't get the \"Client started\" message from " +
            "a client");

        if (!server1.getOutput().trim().equals("Server started!"))
            return CheckResult.wrong("Can't get the \"Server started!\" message from a " +
            "server");

        client1.execute(fromClientMsg);
        sleep(executePause);

        if (!server1.getOutput().trim().equals(fromClientMsg))
            return CheckResult.wrong("A server showed wrong message");

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult test2() {
        final String fromServerMsg1 = "Fuzzy Buzzy!";
        final String fromServerMsg2 = "The Batman is here!";
        final TestedProgram server2 = new TestedProgram(Server.class);
        final TestedProgram client2 = new TestedProgram(Client.class);
        client2.setReturnOutputAfterExecution(false);

        client2.startInBackground();
        server2.start();
        sleep(executePause);
        client2.getOutput();

        server2.execute(fromServerMsg1);
        server2.execute(fromServerMsg2);
        sleep(executePause);

        final String answerFromServer = client2.getOutput().trim();

        if (answerFromServer.equals(fromServerMsg2 + "\n" + fromServerMsg1))
            return CheckResult.wrong("A client showed messages in wrong order!");

        if (!answerFromServer.equals(fromServerMsg1 + "\n" + fromServerMsg2))
            return CheckResult.wrong("A client showed wrong message");


        return CheckResult.correct();
    }

}