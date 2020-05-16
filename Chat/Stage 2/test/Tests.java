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
        TestedProgram server = new TestedProgram(Server.class);
        TestedProgram client = new TestedProgram(Client.class);
        final int executePause = 25;
        final int startAppsPause = 50; //50 is guaranty (c)
        final String fromServerMsg1 = "Fuzzy Buzzy!";
        final String fromServerMsg2 = "The Batman is here!";
        final String fromClientMsg = "You are in my mind!";

        server.startInBackground();
        client.start();
        sleep(startAppsPause);

        client.execute(fromClientMsg);
        sleep(executePause);

        if (!server.getOutput().trim().equals(fromClientMsg))
            return CheckResult.wrong("server showed wrong message");

        server.stop();
        client.stop();

        server = new TestedProgram(Server.class); //apps can't be twice started
        client = new TestedProgram(Client.class);

        client.startInBackground();
        server.start();
        sleep(startAppsPause);

        server.execute(fromServerMsg1);
        server.execute(fromServerMsg2);
        sleep(executePause);

        final String answerFromServer = client.getOutput().trim();

        if (answerFromServer.equals(fromServerMsg2 + "\n" + fromServerMsg1))
            return CheckResult.wrong("client showed messages in wrong order!");

        if (!answerFromServer.equals(fromServerMsg1 + "\n" + fromServerMsg2))
            return CheckResult.wrong("client showed wrong message");


        return CheckResult.correct();
    }

}