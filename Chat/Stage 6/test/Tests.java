import chat.client.Client;
import chat.server.Server;
import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import static org.hyperskill.hstest.common.Utils.sleep;

public class Tests extends StageTest<String> {
    private final int executePause = 50;

    @DynamicTestingMethod
    CheckResult Stage5() {
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

        client1.execute("bla bla bla");
        sleep(executePause);
        final String client1Answer1 = client1.getOutput().trim();
        if (!client1Answer1.equals("Server: you are not in the chat!"))
            return CheckResult.wrong(
            "Can't get the \"Server: you are not in the chat!\" message after " +
            "trying to send a message before auth or register");

        client1.execute("/auth asdasd asdasd");
        sleep(executePause);
        final String client1Answer2 = client1.getOutput().trim();
        if (!client1Answer2.equals("Server: incorrect login!"))
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

        client2.execute("before choosing an addressee");
        sleep(executePause);
        final String client2Answer3 = client2.getOutput().trim();
        if (!client2Answer3.equals("Server: use /list command to choose an user to text!"))
            return CheckResult.wrong("Can't get the " +
            "\"Server: use /list command to choose an user to text!\" message " +
            "before choosing an addressee");

        client2.execute("/list");
        sleep(executePause);
        final String client2Answer4 = client2.getOutput().trim();
        if (!client2Answer4.equals("Server: no one online"))
            return CheckResult.wrong("Can't get the \"Server: no one online\" message " +
            "if there are no online users");

        client2.execute("/exit");
        sleep(executePause);
        if (!client2.isFinished())
            return CheckResult.wrong("A client should be shut down, after the " +
            "\"/exit\" command");

        client1.execute("/auth first paasf");
        sleep(executePause);
        final String client1Answer3 = client1.getOutput().trim();
        if (!client1Answer3.equals("Server: incorrect password!"))
            return CheckResult.wrong(
            "Can't get the \"Server: incorrect password!\" message after " +
            "input a wrong password being logging");

        client1.execute("/auth first 12345678");
        sleep(executePause);
        final String client1Answer4 = client1.getOutput().trim();
        if (!client1Answer4.equals("Server: you are authorized successfully!"))
            return CheckResult.wrong("Can't get the \"Server: you are authorized " +
            "successfully!\" message after successful authentication");

        client3.execute("/registration first 12345678");
        sleep(executePause);
        final String client3Answer1 = client3.getOutput().trim();
        if (!client3Answer1.equals("Server: this login is already in use!"))
            return CheckResult.wrong("Can't get the \"Server: this login is already in " +
            "use!\" message from a client that is trying to register with a login which is " +
            "already in use");

        client3.execute("/registration second 12345678");
        sleep(executePause);
        final String client3Answer2 = client3.getOutput().trim();
        if (!client3Answer2.equals("Server: you are registered successfully!"))
            return CheckResult.wrong("Can't get the \"Server: you are registered " +
            "successfully!\" message after successful authentication");

        client1.execute("/list");
        sleep(executePause);
        final String client1Answer5 = client1.getOutput().trim();
        if (client1Answer5.contains("first"))
            return CheckResult.wrong("The list of onLine users contains the client's " +
            "name, but shouldn't");
        if (!client1Answer5.equals("Server: online: second"))
            return CheckResult.wrong("A client receive a wrong list of online users");

        client1.execute("/chat blabla");
        sleep(executePause);
        final String client1Answer6 = client1.getOutput().trim();
        if (!client1Answer6.equals("Server: the user is not online!"))
            return CheckResult.wrong("Can't get the \"Server: the user is not online!\"" +
            "after try to chat using wrong users' name");

        client1.execute("blabla");
        sleep(executePause);
        final String client1Answer7 = client1.getOutput().trim();
        if (!client1Answer7.equals("Server: use /list command to choose an user to text!"))
            return CheckResult.wrong("Can't get the \"Server: use /list command to " +
            "choose an user to text!\" after try to chat without choosing a user");

        client1.execute("/chat second");
        sleep(executePause);

        client1.execute("test");
        sleep(executePause);
        final String client3Answer3 = client3.getOutput().trim();
        if (!client3Answer3.isEmpty())
            return CheckResult.wrong("A client receive a message but shouldn't");

        client3.execute("/chat first");
        sleep(executePause);
        final String client3Answer4 = client3.getOutput().trim();
        if (!client3Answer4.equals("first: test"))
            return CheckResult.wrong("A client don't receive a message from another one" +
            " or message have wrong format. \"userName: message\"");

        client1.execute("1");
        sleep(executePause);
        client1.execute("2");
        sleep(executePause);
        client1.execute("3");
        sleep(executePause);
        client1.execute("4");
        sleep(executePause);
        client1.execute("5");
        sleep(executePause);
        client1.execute("6");
        sleep(executePause);
        client1.execute("7");
        sleep(executePause);
        client1.execute("8");
        sleep(executePause);
        client1.execute("9");
        sleep(executePause);
        client3.execute("10");
        sleep(executePause);

        final String client1Answer8 = client1.getOutput().trim();
        if (!client1Answer8.equals(
        "first: test\n" +
        "first: 1\n" +
        "first: 2\n" +
        "first: 3\n" +
        "first: 4\n" +
        "first: 5\n" +
        "first: 6\n" +
        "first: 7\n" +
        "first: 8\n" +
        "first: 9\n" +
        "second: 10"))
            return CheckResult.wrong("Client output wrong messages");

        final String client3Answer5 = client3.getOutput().trim();
        if (!client3Answer5.equals(
        "first: 1\n" +
        "first: 2\n" +
        "first: 3\n" +
        "first: 4\n" +
        "first: 5\n" +
        "first: 6\n" +
        "first: 7\n" +
        "first: 8\n" +
        "first: 9\n" +
        "second: 10"))
            return CheckResult.wrong("Client output wrong messages");

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult lastMessagesExtenedEdition() {
        final TestedProgram server2 = new TestedProgram(Server.class);
        final TestedProgram client1 = new TestedProgram(Client.class);
        final TestedProgram client2 = new TestedProgram(Client.class);
        final TestedProgram admin = new TestedProgram(Client.class);
        client1.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        admin.setReturnOutputAfterExecution(false);
        server2.startInBackground();
        sleep(executePause);
        client1.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        admin.start();
        sleep(executePause);
        client1.getOutput();
        client2.getOutput();
        admin.getOutput();

        client1.execute("/auth first 12345678");
        sleep(executePause);
        final String clientAnswer1 = client1.getOutput().trim();
        if (!clientAnswer1.equals("Server: you are authorized successfully!"))
            return CheckResult.wrong("A registered client can't be authenticated after" +
            " rebooting a server");

        client2.execute("/auth second 12345678");
        sleep(executePause);
        client2.execute("/chat first");
        client2.execute("unread");

        client1.execute("/chat second");
        sleep(executePause);
        final String client1Answer2 = client1.getOutput().trim();
        if (!client1Answer2.equals(
        "first: 1\n" +
        "first: 2\n" +
        "first: 3\n" +
        "first: 4\n" +
        "first: 5\n" +
        "first: 6\n" +
        "first: 7\n" +
        "first: 8\n" +
        "first: 9\n" +
        "second: 10\n" +
        "second: unread"))
            return CheckResult.wrong("A client should receive ALL unread messages and " +
            "10 additional last messages from the end of the conversation");

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult kickAndGrand() {
        final TestedProgram server = new TestedProgram(Server.class);
        final TestedProgram client1 = new TestedProgram(Client.class);
        final TestedProgram client2 = new TestedProgram(Client.class);
        final TestedProgram client3 = new TestedProgram(Client.class);
        final TestedProgram admin = new TestedProgram(Client.class);
        client1.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        client3.setReturnOutputAfterExecution(false);
        admin.setReturnOutputAfterExecution(false);
        server.startInBackground();
        sleep(executePause);
        client1.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        client3.start();
        sleep(executePause);
        admin.start();
        sleep(executePause);
        client1.getOutput();
        client2.getOutput();
        client3.getOutput();
        admin.getOutput();

        client1.execute("/auth first 12345678");
        sleep(executePause);
        final String client1Answer1 = client1.getOutput().trim();
        if (!client1Answer1.equals("Server: you are authorized successfully!"))
            return CheckResult.wrong("A registered client can't be authenticated after" +
            " rebooting a server");

        client2.execute("/auth second 12345678");
        sleep(executePause);
        client2.getOutput();

        admin.execute("/auth admin 12345678");
        sleep(executePause);
        final String adminAnswer1 = admin.getOutput().trim();
        if (!adminAnswer1.equals("Server: you are authorized successfully!"))
            return CheckResult.wrong("Admin can't log in!");

        admin.execute("/kick admin");
        sleep(executePause);
        final String adminAnswer2 = admin.getOutput().trim();
        if (!adminAnswer2.equals("Server: you can't kick himself!"))
            return CheckResult.wrong("Can't get the message \"Server: you can't kick " +
            "yourself!\" after admin tried to kick yourself");

        admin.execute("/kick first");
        sleep(executePause);
        final String adminAnswer3 = admin.getOutput().trim();
        if (!adminAnswer3.equals("Server: first was kicked!"))
            return CheckResult.wrong("Can't get the message \"Server: USER was " +
            "kicked!\" after a user was kicked by admin");

        admin.execute("/list");
        sleep(executePause);
        final String adminAnswer4 = admin.getOutput().trim();
        if (!adminAnswer4.equals("Server: online: second"))
            return CheckResult.wrong("Admin received a wrong list of users after kick " +
            "one");

        final String client1Answer2 = client1.getOutput().trim();
        if (!client1Answer2.equals("Server: you have been kicked from the server!"))
            return CheckResult.wrong("Can't get the message \"Server: you have been " +
            "kicked from the server!\" after a successful kicking a user");

        client1.execute("I'm not authed");
        sleep(executePause);
        final String client1Answer3 = client1.getOutput().trim();
        if (!client1Answer3.equals("Server: you are not in the chat!"))
            return CheckResult.wrong("Can't get the message \"Server: you are not in " +
            "the chat!\" after trying to send a message before both auth or register");

        client1.execute("/auth first 12345678");
        sleep(executePause);
        final String client1Answer4 = client1.getOutput().trim();
        if (!client1Answer4.equals("Server: you are banned!"))
            return CheckResult.wrong("Can't get the message \"Server: you are banned!\"");

        client3.execute("/registration first2 12345678");
        sleep(executePause);
        client3.getOutput();

        admin.execute("/grant first2");
        sleep(executePause);
        final String adminAnswer5 = admin.getOutput().trim();
        if (!adminAnswer5.equals("Server: first2 is a new moderator!"))
            return CheckResult.wrong("Can't get the message \"Server: USER is a new " +
            "moderator!\" after a user became a moderator");

        final String client3Answer1 = client3.getOutput().trim();
        if (!client3Answer1.equals("Server: you are a new moderator now!"))
            return CheckResult.wrong("Can't get the message \"Server: you are a new " +
            "moderator now!\" after a user became a moderator");

        admin.execute("/grant first2");
        sleep(executePause);
        final String adminAnswer6 = admin.getOutput().trim();
        if (!adminAnswer6.equals("Server: this user is already a moderator!"))
            return CheckResult.wrong("Can't get the message \"Server: this user is " +
            "already a moderator!\" after cast the \"/grant\" command on a moderator");

        final String client3Answer2 = client3.getOutput().trim();
        if (!client3Answer2.isEmpty())
            return CheckResult.wrong("A moderator shouldn't react on the \"/grant\" " +
            "command!");

        client2.execute("/kick first2");
        sleep(executePause);
        final String client2Answer2 = client2.getOutput().trim();
        if (!client2Answer2.isEmpty())
            return CheckResult.wrong("The server shouldn't react on /kick command" +
            " from user which has not been allowed to use it");

        admin.execute("/list");
        sleep(executePause);
        final String adminAnswer7 = admin.getOutput().trim();
        if (!adminAnswer7.endsWith("first2 second") && !adminAnswer7.endsWith("second " +
        "first2")) //order is insignificant
            return CheckResult.wrong("Admin received a wrong list of users after kick " +
            "one");

        client3.execute("/kick second");
        sleep(executePause);
        final String client3Answer3 = client3.getOutput().trim();
        if (!client3Answer3.equals("Server: second was kicked!"))
            return CheckResult.wrong("Can't get the message \"Server: USER was " +
            "kicked!\" message after a user was kicked by a moderator");

        final String client2Answer1 = client2.getOutput().trim();
        if (!client2Answer1.equals("Server: you have been kicked from the server!"))
            return CheckResult.wrong("Can't get the message \"Server: you have been " +
            "kicked from the server!\" after a successful kicking a user");

        admin.execute("/list");
        sleep(executePause);
        final String adminAnswer8 = admin.getOutput().trim();
        if (!adminAnswer8.equals("Server: online: first2"))
            return CheckResult.wrong("Admin received a wrong list of users after kick " +
            "one");

        client3.execute("/kick admin");
        sleep(executePause);
        final String client3Answer4 = client3.getOutput().trim();
        if (!client3Answer4.isEmpty())
            return CheckResult.wrong("The server shouldn't react on /kick command" +
            " from a moderator trying to kick an admin");

        client3.execute("/list");
        sleep(executePause);
        final String client3Answer5 = client3.getOutput().trim();
        if (!client3Answer5.equals("Server: online: admin"))
            return CheckResult.wrong("It is look like a moderator kicked an admin");

        admin.execute("/kick first2");
        sleep(executePause);
        final String adminAnswer9 = admin.getOutput().trim();
        if (!adminAnswer9.equals("Server: first2 was kicked!"))
            return CheckResult.wrong("It is look like an admin can't kick a moderator");

        final String client3Answer6 = client3.getOutput().trim();
        if (!client3Answer6.equals("Server: you have been kicked from the server!"))
            return CheckResult.wrong("It is look like an admin can't kick a moderator");

        admin.execute("/list");
        sleep(executePause);
        final String adminAnswer10 = admin.getOutput().trim();
        if (!adminAnswer10.equals("Server: no one online"))
            return CheckResult.wrong("It is look like an admin can't kick a moderator");


        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult revoke() {
        final TestedProgram server = new TestedProgram(Server.class);
        final TestedProgram moderator = new TestedProgram(Client.class);
        final TestedProgram client2 = new TestedProgram(Client.class);
        final TestedProgram admin = new TestedProgram(Client.class);
        moderator.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        admin.setReturnOutputAfterExecution(false);
        server.startInBackground();
        sleep(executePause);
        moderator.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        admin.start();
        sleep(executePause);
        moderator.execute("/registration moderator 12345678");
        sleep(executePause);
        client2.execute("/registration 999 12345678");
        sleep(executePause);
        admin.execute("/auth admin 12345678");
        sleep(executePause);
        admin.execute("/grant moderator");
        sleep(executePause);
        moderator.getOutput();
        client2.getOutput();
        admin.getOutput();

        client2.execute("/revoke admin");
        sleep(executePause);
        final String client2Answer1 = client2.getOutput().trim();
        if (!client2Answer1.isEmpty())
            return CheckResult.wrong("Server should react on the /revoke command only " +
            "if it is using by an admin");

        moderator.execute("/revoke 999");
        sleep(executePause);
        final String moderatorAnswer1 = moderator.getOutput().trim();
        if (!moderatorAnswer1.isEmpty())
            return CheckResult.wrong("Server should react on the /revoke command only " +
            "if it is using by an admin");

        admin.execute("/revoke moderator");
        sleep(executePause);
        final String adminAnswer1 = admin.getOutput().trim();
        if (!adminAnswer1.equals("Server: moderator is no longer a moderator!"))
            return CheckResult.wrong("Can't get the message \"Server: USER is no longer" +
            " a moderator!\" after using the \"/revoke\" command by an admin");

        final String moderatorAnswer2 = moderator.getOutput().trim();
        if (!moderatorAnswer2.equals("Server: you are no longer a moderator!"))
            return CheckResult.wrong("Can't get the message \"Server: you are no longer" +
            " a moderator\" after using the \"/revoke\" command by an admin");

        moderator.execute("/kick 999");
        sleep(executePause);
        final String moderatorAnswer3 = moderator.getOutput().trim();
        if (!moderatorAnswer3.isEmpty())
            return CheckResult.wrong("Server shouldn't react on the /kick command by an" +
            " ex-moderator");

        admin.execute("/list");
        sleep(executePause);
        final String adminAnswer2 = admin.getOutput().trim();
        if (!adminAnswer2.endsWith("999 moderator") && !adminAnswer2.endsWith("moderator " +
        "999")) //order is insignificant
            return CheckResult.wrong("Admin received a wrong list of users after " +
            "trying to kick one by ex-moderator");

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult statsAndUnread() {
        final TestedProgram server = new TestedProgram(Server.class);
        final TestedProgram client1 = new TestedProgram(Client.class);
        final TestedProgram client2 = new TestedProgram(Client.class);
        final TestedProgram admin = new TestedProgram(Client.class);
        client1.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        server.startInBackground();
        sleep(executePause);
        client1.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        admin.start();
        sleep(executePause);
        client1.execute("/registration client1 12345678");
        sleep(executePause);
        client2.execute("/registration client2 12345678");
        sleep(executePause);
        admin.execute("/auth admin 12345678");
        sleep(executePause);
        client1.getOutput();
        client2.getOutput();
        admin.getOutput();

        admin.execute("/chat client2");
        sleep(executePause);

        client1.execute("/chat client2");
        sleep(executePause);
        client1.execute("1");
        sleep(executePause);
        client1.execute("2");
        sleep(executePause);
        client1.execute("3");
        sleep(executePause);

        admin.execute("/chat client2");
        sleep(executePause);
        admin.execute("1");
        sleep(executePause);
        admin.execute("2");
        sleep(executePause);
        admin.execute("3");
        sleep(executePause);

        client2.execute("/unread");
        sleep(executePause);
        final String client2Answer1 = client2.getOutput().trim();
        if (!client2Answer1.equals("Server: unread from: admin(3) client1(3)"))
            return CheckResult.wrong("List of unread messages is not correct");

        admin.execute("/unread");
        sleep(executePause);
        final String adminAnswer1 = admin.getOutput().trim();
        if (!adminAnswer1.equals("Server: no one unread"))
            return CheckResult.wrong("List of unread messages is not correct");

        client2.execute("/chat admin");
        sleep(executePause);
        client2.execute("1");
        sleep(executePause);
        client2.execute("1");
        sleep(executePause);
        client2.execute("1");
        sleep(executePause);

        admin.execute("/chat client2");
        sleep(executePause);

        admin.getOutput();
        admin.execute("/stats");
        sleep(executePause);
        final String adminAnswer2 = admin.getOutput().trim();
        if (!adminAnswer2.equals(
        "Server: \n" +
        "Statistics with client2:\n" +
        "Total messages: 6\n" +
        "Messages from admin: 3\n" +
        "Messages from client2: 3"
        ))
            return CheckResult.wrong("Stats information is not correct");

        admin.execute("1");
        sleep(executePause);

        client2.getOutput();
        client2.execute("/stats");
        sleep(executePause);
        final String client2Answer = client2.getOutput().trim();
        System.out.println(adminAnswer2);
        if (!client2Answer.equals(
        "Server: \n" +
        "Statistics with admin:\n" +
        "Total messages: 7\n" +
        "Messages from client2: 3\n" +
        "Messages from admin: 4"
        ))
            return CheckResult.wrong("Stats information is not correct");

        return CheckResult.correct();
    }

}