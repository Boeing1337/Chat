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
        final int executePause = 50;

        server.startInBackground();
        sleep(executePause);
        client1.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        client3.start();
        sleep(executePause);
        client3.getOutput();
        client2.getOutput();

        final String client1Start = client1.getOutput().trim();
        if (!"Client started!\nServer: write your name".equals(client1Start.trim()))
            return CheckResult.wrong("Can't get the \"Client started!\nServer: write " +
            "your name\" messages");

        client1.execute("First");
        sleep(executePause);

        final String client1Answer1 = client1.getOutput().trim();
        if (!client1Answer1.isEmpty())
            return CheckResult.wrong("Client receive a message after successful " +
            "login, but shouldn't");

        client1.execute("Hello all!");
        sleep(executePause);

        final String client1Answer2 = client1.getOutput().trim();
        if (client1Answer2.isEmpty() || !client1Answer2.equals("First: Hello all!"))
            return CheckResult.wrong("Client receive wrong message");


        final String client2Answer1 = client2.getOutput().trim();
        if (client2Answer1.trim().equals("First: Hello all!"))
            return CheckResult.wrong("Client print a message from chat before login " +
            "yet!");
        if (!client2Answer1.isEmpty())
            return CheckResult.wrong("Client print a message before login but shouldn't");

        client2.execute("Second");
        sleep(executePause);

        final String client2Answer2 = client2.getOutput().trim();
        if (!client2Answer2.equals("First: Hello all!"))
            return CheckResult.wrong("Client should receive and print last 10 messages " +
            "after login");

        client3.execute("First");
        sleep(executePause);

        final String client3Answer1 = client3.getOutput().trim();
        if (client3Answer1.isEmpty() || !client3Answer1.trim().equals("Server: This name is in use! Choose " +
        "another one"))
            return CheckResult.wrong("Can't get the \"Server: This name is in use! " +
            "Choose another one\" message after login with name that already in use");

        client3.execute("Second");
        sleep(executePause);

        final String client3Answer2 = client3.getOutput().trim();
        if (client3Answer2.isEmpty() || !client3Answer2.trim().equals("Server: This name is in use! Choose " +
        "another one"))
            return CheckResult.wrong("Can't get the \"Server: This name is in use! " +
            "Choose another one\" message after login with name than already in use");

        client2.execute("Bye bye!");
        sleep(executePause);

        final String client1Answer3 = client1.getOutput().trim();
        final String client2Answer3 = client2.getOutput().trim();

        if (client1Answer3.isEmpty() || client2Answer3.isEmpty())
            return CheckResult.wrong("Client didn't receive a message");

        if (!client1Answer3.equals("Second: Bye bye!") || !client2Answer3.equals("Second: Bye " +
        "bye!"))
            return CheckResult.wrong("Client receive a wrong message");

        client2.execute("First message");
        sleep(executePause);
        client2.execute("Second message");
        sleep(executePause);
        client2.execute("Third message");
        sleep(executePause);
        client2.execute("Fourth message");
        sleep(executePause);
        client2.execute("Fifth message");
        sleep(executePause);
        client2.execute("Sixth message");
        sleep(executePause);
        client2.execute("Seventh message");
        sleep(executePause);
        client2.execute("Eighth message");
        sleep(executePause);
        client2.execute("Ninth message");
        sleep(executePause);
        client2.execute("Tenth message");
        sleep(executePause);
        client2.execute("/exit");
        sleep(executePause);

        if (!client2.isFinished())
            return CheckResult.wrong("Client's program should shut down after /exit " +
            "command");

        client3.execute("Third");
        sleep(executePause);

        final String client3Answer3 = client3.getOutput().trim();
        if (!client3Answer3.equals(
        "Second: First message\n" +
        "Second: Second message\n" +
        "Second: Third message\n" +
        "Second: Fourth message\n" +
        "Second: Fifth message\n" +
        "Second: Sixth message\n" +
        "Second: Seventh message\n" +
        "Second: Eighth message\n" +
        "Second: Ninth message\n" +
        "Second: Tenth message"))
            return CheckResult.wrong("Client should receive and print 10 last messages " +
            "after " +
            "login");


        return CheckResult.correct();
    }
}
