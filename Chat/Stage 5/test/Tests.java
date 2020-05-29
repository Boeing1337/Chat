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

        client2.execute("/registration first pass");
        sleep(executePause);
        final String client2Answer1 = client2.getOutput().trim();
        if (!client2Answer1.equals("Server: the password is too short!"))
            return CheckResult.wrong(
            "Can't get the \"Server: the password is too short!\" message after " +
            "try to registry with short password");

        client2.execute("/registration first 12345678");
        sleep(executePause);
        final String client2Answer2 = client2.getOutput().trim();
        if (!client2Answer2.equals("Server: you are registered successfully!"))
            return CheckResult.wrong("Can't get the \"Server: you are registered " +
            "successfully!\" message after successful authentication");

        client2.execute("/exit");
        sleep(executePause);
        if (!client2.isFinished())
            return CheckResult.wrong("A client should be shut down, after the " +
            "\"/exit\" command");

        client1.execute("/auth first paasf");
        sleep(executePause);
        final String client1Answer2 = client1.getOutput().trim();
        if (!client1Answer2.equals("Server: incorrect password!"))
            return CheckResult.wrong(
            "Can't get the \"Server: incorrect password!\" message after " +
            "input a wrong password being logging");

        client1.execute("/auth first 12345678");
        sleep(executePause);
        final String client1Answer3 = client1.getOutput().trim();
        if (!client1Answer3.equals("Server: you are authorized successfully!"))
            return CheckResult.wrong("Can't get the \"Server: you are authorized " +
            "successfully!\" message after successful authentication");

        client3.execute("/registration second 12345678");
        sleep(executePause);
        final String client3Answer1 = client3.getOutput().trim();
        if (!client3Answer1.equals("Server: you are registered successfully!"))
            return CheckResult.wrong("Can't get the \"Server: you are registered " +
            "successfully!\" message after successful authentication");

        client1.execute("/list");
        sleep(executePause);
        final String client1Answer4 = client1.getOutput().trim();
        if (client1Answer4.contains("first"))
            return CheckResult.wrong("The list of onLine users contains the client's " +
            "name, but shouldn't");
        if (!client1Answer4.equals("Server: online: second"))
            return CheckResult.wrong("A client receive a wrong list of online users");

        client1.execute("/chat blabla");
        sleep(executePause);
        final String client1Answer5 = client1.getOutput().trim();
        if (!client1Answer5.equals("Server: the user is not online!"))
            return CheckResult.wrong("Can't get the \"Server: the user is not online!\"" +
            "after try to chat using wrong users' name");

        client1.execute("/chat second");
        sleep(executePause);

        client1.execute("test");
        sleep(executePause);
        final String client3Answer2 = client3.getOutput().trim();
        if (!client3Answer2.isEmpty())
            return CheckResult.wrong("A client receive a message but shouldn't");

        client3.execute("/chat first");
        sleep(executePause);
        final String client3Answer3 = client3.getOutput().trim();
        System.out.println(client3Answer3);
        if (!client3Answer3.equals("first: test"))
            return CheckResult.wrong("A client don't receive a message from another one" +
            " or message have wrong format. \"userName: message\"");


        return CheckResult.correct();
    }
}