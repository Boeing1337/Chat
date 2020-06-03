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
        client1.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        client3.setReturnOutputAfterExecution(false);
        final String countIs = "Count is ";
        final int executePause = 50;

        server.startInBackground();

        //////Client 1

        client1.start();
        sleep(executePause);

        final String client1Start = client1.getOutput().trim();
        if (!"Client started!".equals(client1Start))
            return CheckResult.wrong("Can't get the \"Client started!\" message");

        client1.execute("1 2 3");
        sleep(executePause);

        final String client1Answer1 = client1.getOutput().trim();
        if (!(countIs + "3").equals(client1Answer1))
            return CheckResult.wrong("Client showed a wrong answer!");

        client1.execute("1 2");
        sleep(executePause);

        final String client1Answer2 = client1.getOutput().trim();
        if (!(countIs + "2").equals(client1Answer2))
            return CheckResult.wrong("Client showed a wrong answer!");

        client1.execute("/exit");
        sleep(executePause);

        //////Client 2

        client2.start();
        sleep(executePause);
        client2.getOutput();

        client2.execute("By my hands");
        sleep(executePause);

        final String client2Answer1 = client2.getOutput().trim();
        if (!(countIs + "3").equals(client2Answer1))
            return CheckResult.wrong("Client showed a wrong answer!");

        /////Client 3

        client3.start();
        sleep(executePause);
        client3.getOutput();

        client3.execute("Zzzz.");
        sleep(executePause);

        final String client3Answer1 = client3.getOutput().trim();
        if (!(countIs + "1").equals(client3Answer1))
            return CheckResult.wrong("Client showed a wrong answer!");

        client3.execute("want to sleep");
        sleep(executePause);

        final String client3Answer2 = client3.getOutput().trim();
        if (!(countIs + "3").equals(client3Answer2))
            return CheckResult.wrong("Client showed a wrong answer!");

        client3.execute("/exit");

        //////Client 2 AGAIN

        client2.execute("Repeat");
        sleep(executePause);

        final String client2Answer2 = client2.getOutput().trim();
        if (!(countIs + "1").equals(client2Answer2))
            return CheckResult.wrong("Client showed a wrong answer!");

        client2.execute("/exit");
        sleep(executePause);

        //////Server

        if (!server.getOutput().trim().equals(
        "Server started!\n" +

        "Client 1 connected!\n" +
        "Client 1 sent: 1 2 3\n" +
        "Sent to client 1: " + countIs + "3\n" +
        "Client 1 sent: 1 2\n" +
        "Sent to client 1: " + countIs + "2\n" +
        "Client 1 disconnected!\n" +

        "Client 2 connected!\n" +
        "Client 2 sent: By my hands\n" +
        "Sent to client 2: " + countIs + "3\n" +

        "Client 3 connected!\n" +
        "Client 3 sent: Zzzz.\n" +
        "Sent to client 3: " + countIs + "1\n" +

        "Client 3 sent: want to sleep\n" +
        "Sent to client 3: " + countIs + "3\n" +
        "Client 3 disconnected!\n" +

        "Client 2 sent: Repeat\n" +
        "Sent to client 2: " + countIs + "1\n" +
        "Client 2 disconnected!"
        ))
            return CheckResult.wrong(
            "Server showed wrong messages or messages in wrong order");


        return CheckResult.correct();
    }

}
