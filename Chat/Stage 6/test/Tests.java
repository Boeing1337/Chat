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
        if (!"Client started!\nServer: authorize or register".equals(client1Start.trim()))
            return CheckResult.wrong("Can't get the \"Client started!\nServer: " +
            "authorize or register\" messages");

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
        "(new) second: 10"))
            return CheckResult.wrong("Client output wrong messages");

        final String client3Answer5 = client3.getOutput().trim();
        if (!client3Answer5.equals(
        "(new) first: 1\n" +
        "(new) first: 2\n" +
        "(new) first: 3\n" +
        "(new) first: 4\n" +
        "(new) first: 5\n" +
        "(new) first: 6\n" +
        "(new) first: 7\n" +
        "(new) first: 8\n" +
        "(new) first: 9\n" +
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
        sleep(executePause);

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
        if (!adminAnswer2.equals("Server: you can't kick yourself!"))
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
        "Server:\n" +
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
        "Server:\n" +
        "Statistics with admin:\n" +
        "Total messages: 7\n" +
        "Messages from client2: 3\n" +
        "Messages from admin: 4"
        ))
            return CheckResult.wrong("Stats information is not correct");

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult history100and250() {
        final TestedProgram server = new TestedProgram(Server.class);
        final TestedProgram client3 = new TestedProgram(Client.class);
        final TestedProgram client4 = new TestedProgram(Client.class);
        client3.setReturnOutputAfterExecution(false);
        client4.setReturnOutputAfterExecution(false);
        server.startInBackground();
        sleep(executePause);
        client3.start();
        sleep(executePause);
        client4.start();
        sleep(executePause);
        client3.execute("/registration client3 12345678");
        sleep(executePause);
        client4.execute("/registration client4 12345678");
        sleep(executePause);
        client3.getOutput();
        client4.getOutput();

        client3.execute("/chat client4");
        sleep(executePause);
        // client4.execute("/chat client3");
        sleep(executePause);

        client3.execute("1");
        sleep(executePause);
        client3.execute("2");
        sleep(executePause);
        client3.execute("3");
        sleep(executePause);
        client3.execute("4");
        sleep(executePause);
        client3.execute("5");
        sleep(executePause);
        client3.execute("6");
        sleep(executePause);
        client3.execute("7");
        sleep(executePause);
        client3.execute("8");
        sleep(executePause);
        client3.execute("9");
        sleep(executePause);
        client3.execute("10");
        sleep(executePause);
        client3.execute("11");
        sleep(executePause);
        client3.execute("12");
        sleep(executePause);
        client3.execute("13");
        sleep(executePause);
        client3.execute("14");
        sleep(executePause);
        client3.execute("15");
        sleep(executePause);
        client3.execute("16");
        sleep(executePause);
        client3.execute("17");
        sleep(executePause);
        client3.execute("18");
        sleep(executePause);
        client3.execute("19");
        sleep(executePause);
        client3.execute("20");
        sleep(executePause);
        client3.execute("21");
        sleep(executePause);
        client3.execute("22");
        sleep(executePause);
        client3.execute("23");
        sleep(executePause);
        client3.execute("24");
        sleep(executePause);
        client3.execute("25");
        sleep(executePause);
        client3.execute("26");
        sleep(executePause);
        client3.execute("27");
        sleep(executePause);
        client3.execute("28");
        sleep(executePause);
        client3.execute("29");
        sleep(executePause);
        client3.execute("30");
        sleep(executePause);
        client3.execute("31");
        sleep(executePause);
        client3.execute("32");
        sleep(executePause);
        client3.execute("33");
        sleep(executePause);
        client3.execute("34");
        sleep(executePause);
        client3.execute("35");
        sleep(executePause);
        client3.execute("36");
        sleep(executePause);
        client3.execute("37");
        sleep(executePause);
        client3.execute("38");
        sleep(executePause);
        client3.execute("39");
        sleep(executePause);
        client3.execute("40");
        sleep(executePause);
        client3.execute("41");
        sleep(executePause);
        client3.execute("42");
        sleep(executePause);
        client3.execute("43");
        sleep(executePause);
        client3.execute("44");
        sleep(executePause);
        client3.execute("45");
        sleep(executePause);
        client3.execute("46");
        sleep(executePause);
        client3.execute("47");
        sleep(executePause);
        client3.execute("48");
        sleep(executePause);
        client3.execute("49");
        sleep(executePause);
        client3.execute("50");
        sleep(executePause);
        client3.execute("51");
        sleep(executePause);
        client3.execute("52");
        sleep(executePause);
        client3.execute("53");
        sleep(executePause);
        client3.execute("54");
        sleep(executePause);
        client3.execute("55");
        sleep(executePause);
        client3.execute("56");
        sleep(executePause);
        client3.execute("57");
        sleep(executePause);
        client3.execute("58");
        sleep(executePause);
        client3.execute("59");
        sleep(executePause);
        client3.execute("60");
        sleep(executePause);
        client3.execute("61");
        sleep(executePause);
        client3.execute("62");
        sleep(executePause);
        client3.execute("63");
        sleep(executePause);
        client3.execute("64");
        sleep(executePause);
        client3.execute("65");
        sleep(executePause);
        client3.execute("66");
        sleep(executePause);
        client3.execute("67");
        sleep(executePause);
        client3.execute("68");
        sleep(executePause);
        client3.execute("69");
        sleep(executePause);
        client3.execute("70");
        sleep(executePause);
        client3.execute("71");
        sleep(executePause);
        client3.execute("72");
        sleep(executePause);
        client3.execute("73");
        sleep(executePause);
        client3.execute("74");
        sleep(executePause);
        client3.execute("75");
        sleep(executePause);
        client3.execute("76");
        sleep(executePause);
        client3.execute("77");
        sleep(executePause);
        client3.execute("78");
        sleep(executePause);
        client3.execute("79");
        sleep(executePause);
        client3.execute("80");
        sleep(executePause);
        client3.execute("81");
        sleep(executePause);
        client3.execute("82");
        sleep(executePause);
        client3.execute("83");
        sleep(executePause);
        client3.execute("84");
        sleep(executePause);
        client3.execute("85");
        sleep(executePause);
        client3.execute("86");
        sleep(executePause);
        client3.execute("87");
        sleep(executePause);
        client3.execute("88");
        sleep(executePause);
        client3.execute("89");
        sleep(executePause);
        client3.execute("90");
        sleep(executePause);
        client3.execute("91");
        sleep(executePause);
        client3.execute("92");
        sleep(executePause);
        client3.execute("93");
        sleep(executePause);
        client3.execute("94");
        sleep(executePause);
        client3.execute("95");
        sleep(executePause);
        client3.execute("96");
        sleep(executePause);
        client3.execute("97");
        sleep(executePause);
        client3.execute("98");
        sleep(executePause);
        client3.execute("99");
        sleep(executePause);
        client3.execute("100");
        sleep(executePause);
        client3.execute("101");
        sleep(executePause);
        client3.execute("102");
        sleep(executePause);
        client3.execute("103");
        sleep(executePause);
        client3.execute("104");
        sleep(executePause);
        client3.execute("105");
        sleep(executePause);
        client3.execute("106");
        sleep(executePause);
        client3.execute("107");
        sleep(executePause);
        client3.execute("108");
        sleep(executePause);
        client3.execute("109");
        sleep(executePause);
        client3.execute("110");
        sleep(executePause);
        client3.execute("111");
        sleep(executePause);
        client3.execute("112");
        sleep(executePause);
        client3.execute("113");
        sleep(executePause);
        client3.execute("114");
        sleep(executePause);
        client3.execute("115");
        sleep(executePause);
        client3.execute("116");
        sleep(executePause);
        client3.execute("117");
        sleep(executePause);
        client3.execute("118");
        sleep(executePause);
        client3.execute("119");
        sleep(executePause);
        client3.execute("120");
        sleep(executePause);
        client3.execute("121");
        sleep(executePause);
        client3.execute("122");
        sleep(executePause);
        client3.execute("123");
        sleep(executePause);
        client3.execute("124");
        sleep(executePause);
        client3.execute("125");
        sleep(executePause);
        client3.execute("126");
        sleep(executePause);
        client3.execute("127");
        sleep(executePause);
        client3.execute("128");
        sleep(executePause);
        client3.execute("129");
        sleep(executePause);
        client3.execute("130");
        sleep(executePause);
        client3.execute("131");
        sleep(executePause);
        client3.execute("132");
        sleep(executePause);
        client3.execute("133");
        sleep(executePause);
        client3.execute("134");
        sleep(executePause);
        client3.execute("135");
        sleep(executePause);
        client3.execute("136");
        sleep(executePause);
        client3.execute("137");
        sleep(executePause);
        client3.execute("138");
        sleep(executePause);
        client3.execute("139");
        sleep(executePause);
        client3.execute("140");
        sleep(executePause);
        client3.execute("141");
        sleep(executePause);
        client3.execute("142");
        sleep(executePause);
        client3.execute("143");
        sleep(executePause);
        client3.execute("144");
        sleep(executePause);
        client3.execute("145");
        sleep(executePause);
        client3.execute("146");
        sleep(executePause);
        client3.execute("147");
        sleep(executePause);
        client3.execute("148");
        sleep(executePause);
        client3.execute("149");
        sleep(executePause);
        client3.execute("150");
        sleep(executePause);
        client3.execute("151");
        sleep(executePause);
        client3.execute("152");
        sleep(executePause);
        client3.execute("153");
        sleep(executePause);
        client3.execute("154");
        sleep(executePause);
        client3.execute("155");
        sleep(executePause);
        client3.execute("156");
        sleep(executePause);
        client3.execute("157");
        sleep(executePause);
        client3.execute("158");
        sleep(executePause);
        client3.execute("159");
        sleep(executePause);
        client3.execute("160");
        sleep(executePause);
        client3.execute("161");
        sleep(executePause);
        client3.execute("162");
        sleep(executePause);
        client3.execute("163");
        sleep(executePause);
        client3.execute("164");
        sleep(executePause);
        client3.execute("165");
        sleep(executePause);
        client3.execute("166");
        sleep(executePause);
        client3.execute("167");
        sleep(executePause);
        client3.execute("168");
        sleep(executePause);
        client3.execute("169");
        sleep(executePause);
        client3.execute("170");
        sleep(executePause);
        client3.execute("171");
        sleep(executePause);
        client3.execute("172");
        sleep(executePause);
        client3.execute("173");
        sleep(executePause);
        client3.execute("174");
        sleep(executePause);
        client3.execute("175");
        sleep(executePause);
        client3.execute("176");
        sleep(executePause);
        client3.execute("177");
        sleep(executePause);
        client3.execute("178");
        sleep(executePause);
        client3.execute("179");
        sleep(executePause);
        client3.execute("180");
        sleep(executePause);
        client3.execute("181");
        sleep(executePause);
        client3.execute("182");
        sleep(executePause);
        client3.execute("183");
        sleep(executePause);
        client3.execute("184");
        sleep(executePause);
        client3.execute("185");
        sleep(executePause);
        client3.execute("186");
        sleep(executePause);
        client3.execute("187");
        sleep(executePause);
        client3.execute("188");
        sleep(executePause);
        client3.execute("189");
        sleep(executePause);
        client3.execute("190");
        sleep(executePause);
        client3.execute("191");
        sleep(executePause);
        client3.execute("192");
        sleep(executePause);
        client3.execute("193");
        sleep(executePause);
        client3.execute("194");
        sleep(executePause);
        client3.execute("195");
        sleep(executePause);
        client3.execute("196");
        sleep(executePause);
        client3.execute("197");
        sleep(executePause);
        client3.execute("198");
        sleep(executePause);
        client3.execute("199");
        sleep(executePause);
        client3.execute("200");
        sleep(executePause);
        client3.execute("201");
        sleep(executePause);
        client3.execute("202");
        sleep(executePause);
        client3.execute("203");
        sleep(executePause);
        client3.execute("204");
        sleep(executePause);
        client3.execute("205");
        sleep(executePause);
        client3.execute("206");
        sleep(executePause);
        client3.execute("207");
        sleep(executePause);
        client3.execute("208");
        sleep(executePause);
        client3.execute("209");
        sleep(executePause);
        client3.execute("210");
        sleep(executePause);
        client3.execute("211");
        sleep(executePause);
        client3.execute("212");
        sleep(executePause);
        client3.execute("213");
        sleep(executePause);
        client3.execute("214");
        sleep(executePause);
        client3.execute("215");
        sleep(executePause);
        client3.execute("216");
        sleep(executePause);
        client3.execute("217");
        sleep(executePause);
        client3.execute("218");
        sleep(executePause);
        client3.execute("219");
        sleep(executePause);
        client3.execute("220");
        sleep(executePause);
        client3.execute("221");
        sleep(executePause);
        client3.execute("222");
        sleep(executePause);
        client3.execute("223");
        sleep(executePause);
        client3.execute("224");
        sleep(executePause);
        client3.execute("225");
        sleep(executePause);
        client3.execute("226");
        sleep(executePause);
        client3.execute("227");
        sleep(executePause);
        client3.execute("228");
        sleep(executePause);
        client3.execute("229");
        sleep(executePause);
        client3.execute("230");
        sleep(executePause);
        client3.execute("231");
        sleep(executePause);
        client3.execute("232");
        sleep(executePause);
        client3.execute("233");
        sleep(executePause);
        client3.execute("234");
        sleep(executePause);
        client3.execute("235");
        sleep(executePause);
        client3.execute("236");
        sleep(executePause);
        client3.execute("237");
        sleep(executePause);
        client3.execute("238");
        sleep(executePause);
        client3.execute("239");
        sleep(executePause);
        client3.execute("240");
        sleep(executePause);
        client3.execute("241");
        sleep(executePause);
        client3.execute("242");
        sleep(executePause);
        client3.execute("243");
        sleep(executePause);
        client3.execute("244");
        sleep(executePause);
        client3.execute("245");
        sleep(executePause);
        client3.execute("246");
        sleep(executePause);
        client3.execute("247");
        sleep(executePause);
        client3.execute("248");
        sleep(executePause);
        client3.execute("249");
        sleep(executePause);
        client3.execute("250");
        sleep(executePause);


        client4.execute("/chat client3");
        sleep(executePause);
        sleep(executePause);

        final String client4Answer1 = client4.getOutput().trim();
        if (!client4Answer1.equals(
        "client3: 151\n" +
        "client3: 152\n" +
        "client3: 153\n" +
        "client3: 154\n" +
        "client3: 155\n" +
        "client3: 156\n" +
        "client3: 157\n" +
        "client3: 158\n" +
        "client3: 159\n" +
        "client3: 160\n" +
        "client3: 161\n" +
        "client3: 162\n" +
        "client3: 163\n" +
        "client3: 164\n" +
        "client3: 165\n" +
        "client3: 166\n" +
        "client3: 167\n" +
        "client3: 168\n" +
        "client3: 169\n" +
        "client3: 170\n" +
        "client3: 171\n" +
        "client3: 172\n" +
        "client3: 173\n" +
        "client3: 174\n" +
        "client3: 175\n" +
        "client3: 176\n" +
        "client3: 177\n" +
        "client3: 178\n" +
        "client3: 179\n" +
        "client3: 180\n" +
        "client3: 181\n" +
        "client3: 182\n" +
        "client3: 183\n" +
        "client3: 184\n" +
        "client3: 185\n" +
        "client3: 186\n" +
        "client3: 187\n" +
        "client3: 188\n" +
        "client3: 189\n" +
        "client3: 190\n" +
        "client3: 191\n" +
        "client3: 192\n" +
        "client3: 193\n" +
        "client3: 194\n" +
        "client3: 195\n" +
        "client3: 196\n" +
        "client3: 197\n" +
        "client3: 198\n" +
        "client3: 199\n" +
        "client3: 200\n" +
        "client3: 201\n" +
        "client3: 202\n" +
        "client3: 203\n" +
        "client3: 204\n" +
        "client3: 205\n" +
        "client3: 206\n" +
        "client3: 207\n" +
        "client3: 208\n" +
        "client3: 209\n" +
        "client3: 210\n" +
        "client3: 211\n" +
        "client3: 212\n" +
        "client3: 213\n" +
        "client3: 214\n" +
        "client3: 215\n" +
        "client3: 216\n" +
        "client3: 217\n" +
        "client3: 218\n" +
        "client3: 219\n" +
        "client3: 220\n" +
        "client3: 221\n" +
        "client3: 222\n" +
        "client3: 223\n" +
        "client3: 224\n" +
        "client3: 225\n" +
        "client3: 226\n" +
        "client3: 227\n" +
        "client3: 228\n" +
        "client3: 229\n" +
        "client3: 230\n" +
        "client3: 231\n" +
        "client3: 232\n" +
        "client3: 233\n" +
        "client3: 234\n" +
        "client3: 235\n" +
        "client3: 236\n" +
        "client3: 237\n" +
        "client3: 238\n" +
        "client3: 239\n" +
        "client3: 240\n" +
        "client3: 241\n" +
        "client3: 242\n" +
        "client3: 243\n" +
        "client3: 244\n" +
        "client3: 245\n" +
        "client3: 246\n" +
        "client3: 247\n" +
        "client3: 248\n" +
        "client3: 249\n" +
        "client3: 250"
        ))
            return CheckResult.wrong("A user get wrong messages. Maybe you server " +
            "sent more than 100 unread messages.");

        client4.execute("/history test");
        sleep(executePause);
        final String client4Answer2 = client4.getOutput().trim();
        if (!client4Answer2.equals("Server: test is not a number!"))
            return CheckResult.wrong("Can't get the message \"Server: X is not a " +
            "number!\"");

        client4.execute("/history 250");
        sleep(executePause);
        final String client4Answer3 = client4.getOutput().trim();
        if (!client4Answer3.equals(
        "Server:\n" +
        "client3: 1\n" +
        "client3: 2\n" +
        "client3: 3\n" +
        "client3: 4\n" +
        "client3: 5\n" +
        "client3: 6\n" +
        "client3: 7\n" +
        "client3: 8\n" +
        "client3: 9\n" +
        "client3: 10\n" +
        "client3: 11\n" +
        "client3: 12\n" +
        "client3: 13\n" +
        "client3: 14\n" +
        "client3: 15\n" +
        "client3: 16\n" +
        "client3: 17\n" +
        "client3: 18\n" +
        "client3: 19\n" +
        "client3: 20\n" +
        "client3: 21\n" +
        "client3: 22\n" +
        "client3: 23\n" +
        "client3: 24\n" +
        "client3: 25\n" +
        "client3: 26\n" +
        "client3: 27\n" +
        "client3: 28\n" +
        "client3: 29\n" +
        "client3: 30\n" +
        "client3: 31\n" +
        "client3: 32\n" +
        "client3: 33\n" +
        "client3: 34\n" +
        "client3: 35\n" +
        "client3: 36\n" +
        "client3: 37\n" +
        "client3: 38\n" +
        "client3: 39\n" +
        "client3: 40\n" +
        "client3: 41\n" +
        "client3: 42\n" +
        "client3: 43\n" +
        "client3: 44\n" +
        "client3: 45\n" +
        "client3: 46\n" +
        "client3: 47\n" +
        "client3: 48\n" +
        "client3: 49\n" +
        "client3: 50\n" +
        "client3: 51\n" +
        "client3: 52\n" +
        "client3: 53\n" +
        "client3: 54\n" +
        "client3: 55\n" +
        "client3: 56\n" +
        "client3: 57\n" +
        "client3: 58\n" +
        "client3: 59\n" +
        "client3: 60\n" +
        "client3: 61\n" +
        "client3: 62\n" +
        "client3: 63\n" +
        "client3: 64\n" +
        "client3: 65\n" +
        "client3: 66\n" +
        "client3: 67\n" +
        "client3: 68\n" +
        "client3: 69\n" +
        "client3: 70\n" +
        "client3: 71\n" +
        "client3: 72\n" +
        "client3: 73\n" +
        "client3: 74\n" +
        "client3: 75\n" +
        "client3: 76\n" +
        "client3: 77\n" +
        "client3: 78\n" +
        "client3: 79\n" +
        "client3: 80\n" +
        "client3: 81\n" +
        "client3: 82\n" +
        "client3: 83\n" +
        "client3: 84\n" +
        "client3: 85\n" +
        "client3: 86\n" +
        "client3: 87\n" +
        "client3: 88\n" +
        "client3: 89\n" +
        "client3: 90\n" +
        "client3: 91\n" +
        "client3: 92\n" +
        "client3: 93\n" +
        "client3: 94\n" +
        "client3: 95\n" +
        "client3: 96\n" +
        "client3: 97\n" +
        "client3: 98\n" +
        "client3: 99\n" +
        "client3: 100"
        ))
            return CheckResult.wrong("A user receive wrong messages after the " +
            "\"/history X\". Maybe your server sent more than 100 unread messages.");

        return CheckResult.correct();
    }

}