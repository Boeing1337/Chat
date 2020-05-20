import chat.client.Client;
import chat.server.Server;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import static org.hyperskill.hstest.common.Utils.sleep;

public class Tests extends StageTest<String> {
    private final int startAppsPause = 50; //50 is guaranty (c)
    private final int executePause = 25;

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

        server.startInBackground();

        //////Client 1

        client1.start();
        sleep(startAppsPause);

        final String client1Start = client1.getOutput().trim();
        if (!"Client started!".equals(client1Start))
            return CheckResult.wrong("Can't get the \"Client started!\" message");

        client1.execute("1 2 3");
        sleep(executePause);

        String client1Answer = client1.getOutput().trim();
        if (!(countIs + "3").equals(client1Answer))
            return CheckResult.wrong("Client showed a wrong answer!");

        client1.execute("1 2");
        sleep(executePause);

        client1Answer = client1.getOutput().trim();
        if (!(countIs + "2").equals(client1Answer))
            return CheckResult.wrong("Client showed a wrong answer!");

        client1.execute("/exit");
        sleep(executePause);

        //////Client 2

        client2.start();
        sleep(startAppsPause);
        client2.getOutput();

        client2.execute("By my hands");
        sleep(executePause);

        String client2Answer = client2.getOutput().trim();
        if (!(countIs + "3").equals(client2Answer))
            return CheckResult.wrong("Client showed a wrong answer!");

        /////Client 3

        client3.start();
        sleep(startAppsPause);
        client3.getOutput();

        client3.execute("Zzzz.");
        sleep(executePause);

        String client3Answer = client3.getOutput().trim();
        if (!(countIs + "1").equals(client3Answer))
            return CheckResult.wrong("Client showed a wrong answer!");

        client3.execute("want to sleep");
        sleep(executePause);

        client3Answer = client3.getOutput().trim();
        if (!(countIs + "3").equals(client3Answer))
            return CheckResult.wrong("Client showed a wrong answer!");

        client3.execute("/exit");

        //////Client 2 AGAIN

        client2.execute("Repeat");
        sleep(executePause);

        client2Answer = client2.getOutput().trim();
        if (!(countIs + "1").equals(client2Answer))
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
