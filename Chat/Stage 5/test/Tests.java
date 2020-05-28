import chat.client.Client;
import chat.server.Server;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import static org.hyperskill.hstest.common.Utils.sleep;

public class Tests extends StageTest<String> {
    private final int executePause = 50;
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
        if (!"Client started!\nServer: authorize or register.".equals(client1Start.trim()))
            return CheckResult.wrong("Can't get the \"Client started!\nServer: " +
            "authorize or register.\" messages");

        client1.execute("/auth asdasd asdasd");
        sleep(executePause);
        final String client1Answer1 = client1.getOutput().trim();
        if (!client1Answer1.equals("Server: incorrect login!"))
            return CheckResult.wrong(
            "Can't get the \"Server: incorrect login!\" message after " +
            "input wrong login and password");

        client2.execute("/registration login passssss");
        sleep(executePause);
        client2.execute("/exit");
        sleep(executePause);

        client1.execute("/auth login paasf");
        sleep(executePause);
        final String client1Answer2 = client1.getOutput().trim();
        if (!client1Answer2.equals("Server: incorrect password!"))
            return CheckResult.wrong(
            "Can't get the \"Server: incorrect password!\" message after " +
            "input a wrong password being logging");

        client1.execute("/auth login passssss");
        sleep(executePause);
        final String client1Answer3 = client1.getOutput().trim();
        if (!client1Answer3.equals("Server: you are authorized successfully!"))
            return CheckResult.wrong("Can't get the \"Server: you are authorized " +
            "successfully!\" message after " +
            "successful authentication");

        client3.execute("/registration second passssssss");
        sleep(executePause);
        client1.execute("/list");
        sleep(executePause);
        System.out.println(client1.getOutput());

        return CheckResult.correct();
    }
}