package animals.services;

import java.io.FilterInputStream;
import java.util.Scanner;

// eagerly loaded singleton, just for brevity
public class UserInputService {
    public final static String newLine = System.lineSeparator();
    public final static String verticalDots;
    private final static String userInputPlaceholder = "> ";
    private final static UserInputService instance = new UserInputService();

    static {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            builder.append(newLine).append(".");
        }
        verticalDots = builder.toString();
    }

    private UserInputService() {
    }

    public static UserInputService getInstance() {
        return instance;
    }

    public String getInput() {
        // System.in wrapped in FilterInputStream to prevent closing when scanner is closed
        // System.in cannot be reopened after being closed
        try (Scanner scanner = new Scanner(new FilterInputStream(System.in) {
            public void close() {}})) {
            System.out.print(userInputPlaceholder);
            return scanner.nextLine().strip();
        }
    }
}
