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
        final int pause = 50;

        server.startInBackground();
        final String client1Start = client1.start();
        client2.start();
        client3.start();
        sleep(pause);

        //TO DO: can't get output by getOutput method right now.
        // If else startAppPause would be 1000ms

        if (client1Start == null || !"Client started!\nServer: write your name.".equals(client1Start.trim()))
            return CheckResult.wrong("Can't get the \"Client started!\nServer: write your name.\" message");

        String temp1 = client1.execute("First");
        if (!temp1.isEmpty())
            return CheckResult.wrong("Client receive a message after successful " +
            "login, but shouldn't");

        client1.execute("Hello all!");
        sleep(pause);

        temp1 = client1.getOutput();
        if (temp1 == null || !temp1.trim().equals("First: Hello all!"))
            return CheckResult.wrong("Client receive wrong message");

        String temp2 = client2.getOutput();
        if (temp2.trim().equals("First: Hello all!"))
            return CheckResult.wrong("Client print a message from chat before login " +
            "yet!");
        if (!temp2.isEmpty())
            return CheckResult.wrong("Client print a message before login but shouldn't");

        client2.execute("Second");  //TO DO (1.1) execute didn't return String
        sleep(pause);


        temp2 = client2.getOutput().trim();  //TO DO (1.2) String is returned by getOutput
        if (!temp2.equals("First: Hello all!"))
            return CheckResult.wrong("Client should receive and print 10 last messages " +
            "after " +
            "login");

        String temp3 = client3.execute("First");  // TO DO(1.3) execute() return a
                                                        // String instantly
        sleep(50);

        if (temp3.isEmpty() || !temp3.trim().equals("Server: This name is in use! Choose " +
        "another one:"))
            return CheckResult.wrong("Can't get the \"Server: This name is in use! " +
            "Choose another one:\" message after login with name than already in use");

        return CheckResult.correct();
    }
}