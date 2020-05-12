import chat.client.Client;
import chat.server.Server;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

public class Tests extends StageTest<String> {

    @DynamicTestingMethod
    CheckResult test() {
        TestedProgram server = new TestedProgram(Server.class);
        TestedProgram client = new TestedProgram(Client.class);
        final String msgToServer = "Fuzzy Buzzy!";
        final String msgToClient = "The Batman is here!";

        server.start();
        client.start();
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Unexpected exception!\nTest has been aborted.\nPlease " +
            "send the report to support@hyperskill.org");
            e.printStackTrace();
        }

        server.execute(msgToClient);
        client.execute(msgToServer);

        try {
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Unexpected exception!\nTest has been aborted.\nPlease " +
            "send the report to support@hyperskill.org");
            e.printStackTrace();
        }

        String serverShow = server.execute("/show");
        if (serverShow != null && !serverShow.trim().equals(msgToServer))
            return CheckResult.wrong("server showed wrong message");

        String clientShow = client.execute("/show");
        if (clientShow != null && !clientShow.trim().equals(msgToClient))
            return CheckResult.wrong("client showed wrong message");

        System.out.println("TEST COMMIT");
        return CheckResult.correct();
    }

}
