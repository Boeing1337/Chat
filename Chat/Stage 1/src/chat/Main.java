package chat;
// Это всё попытка обмануть Сканер при вводе отрицательного значения в строковую переменную 
import java.util.Scanner;

public class Main {

    public static void main(final String[] args) {
        final Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String rawMsg = scanner.nextLine();
            if (rawMsg.contains(" sent ")) {
                rawMsg = rawMsg.replaceFirst(" sent", ":");
                System.out.println(rawMsg);
            }
        }
        scanner.close();
    }
}
