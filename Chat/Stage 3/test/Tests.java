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
        TestedProgram client2 = new TestedProgram(Client.class);
        TestedProgram client3 = new TestedProgram(Client.class);

        server.start();
        client.start();
        client2.start();
        client3.start();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Unexpected exception!\nTest has been aborted.\nPlease " +
            "send the report to support@hyperskill.org");
            e.printStackTrace();
        }

        client.execute("One One One"); // 1 - 3
        client2.execute("second one"); // 2 - 2
        client2.execute("going to sleep"); // 2 -3

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Unexpected exception!\nTest has been aborted.\nPlease " +
            "send the report to support@hyperskill.org");
            e.printStackTrace();
        }

        String temp = server.execute("/show");
        if (temp == null || !temp.trim().equals("One One One\nsecond one\ngoing to sleep"))
            return CheckResult.wrong("Wrong answer after /show command.");

        client.execute("a  f g     h!!!!"); // 1 - 4
        client3.execute("Hello!"); // 3 - 1
        client.execute("1"); // 1 - 0
        client2.execute("first"); // 2 - 1


        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Unexpected exception!\nTest has been aborted.\nPlease " +
            "send the report to support@hyperskill.org");
            e.printStackTrace();
        }


        String clientExit = client.execute("/exit");
        String client2Exit = client2.execute("/exit");
        String client3Exit = client3.execute("/exit");

        if (clientExit == null || client2Exit == null || client3Exit == null)
            return CheckResult.wrong("Client didn't show message.");

        if (!clientExit.trim().equals("Count is 3\nCount is 4\nCount is 1"))
            return CheckResult.wrong("Wrong answer.");

        if (!client2Exit.trim().equals("Count is 2\nCount is 3\nCount is 1"))
            return CheckResult.wrong("Wrong answer.");

        if (!client3Exit.trim().equals("Count is 1"))
            return CheckResult.wrong("Wrong answer.");


        return CheckResult.correct();
    }

}
