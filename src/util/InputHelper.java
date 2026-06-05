package util;

import java.util.Scanner;

public class InputHelper {
    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readText(String prompt) {
        throw new UnsupportedOperationException("Text input handling is not implemented yet.");
    }

    public int readInt(String prompt) {
        throw new UnsupportedOperationException("Integer input handling is not implemented yet.");
    }

    public Scanner getScanner() {
        return scanner;
    }
}
