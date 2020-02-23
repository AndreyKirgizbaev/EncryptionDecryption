import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String operation = "enc";
        String message = "";
        int key = 0;
        String in = null;
        String out = null;
        String algorithmType = "shift";

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-mode"))
                operation = args[i + 1];
            if (args[i].equals("-key"))
                key = Integer.parseInt(args[i + 1]);
            if (args[i].equals("-data"))
                message = args[i + 1].replace("\"", "");
            if (args[i].equals("-in"))
                in = args[i + 1];
            if (args[i].equals("-out"))
                out = args[i + 1];
            if (args[i].equals("-alg"))
                algorithmType = args[i + 1];
        }

        if (in != null) {
            File file = new File(in);
            try (Scanner scanner = new Scanner(file)) {
                message = scanner.nextLine();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("Error");
            }
        }

        char[] chars = message.toCharArray();

        Algorithm algorithm = null;
        if (algorithmType.equals("shift")) {
            algorithm = new ShiftAlgorithm(chars, operation, key);
        } else if (algorithmType.equals("unicode")) {
            algorithm = new UnicodeAlgorithm(chars, operation, key);
        }

        assert algorithm != null;
        chars = algorithm.doIt();

        if (out != null) {
            try {
                FileWriter fileWriter = new FileWriter(out);
                fileWriter.write(chars);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error");
            }
        } else
            System.out.println(chars);
    }

    abstract static class Algorithm {

        char[] chars;
        String operation;
        int key;

        public Algorithm(char[] chars, String operation, int key) {
            this.chars = chars;
            this.operation = operation;
            this.key = key;
        }

        public char[] doIt() {
            return null;
        }
    }

    /**
     encode/decode only letters
     */
    static class ShiftAlgorithm extends Algorithm {
        public ShiftAlgorithm(char[] chars, String operation, int key) {
            super(chars, operation, key);
        }

        @Override
        public char[] doIt() {
            if (operation.equals("enc")) {
                for (int i = 0; i < chars.length; i++) {
                    char ch = chars[i];
                    if (ch >= 97 && ch <= 122 - key)
                        ch += key;
                    else if (ch > 122 - key && ch <= 122)
                        ch -= 26 - key;
                    chars[i] = ch;
                }
            } else if (operation.equals("dec")) {
                for (int i = 0; i < chars.length; i++) {
                    char ch = chars[i];
                    if (ch >= 97 && ch < 97 + key)
                        ch += 26 - key;
                    else if (ch >= 97 + key && ch <= 122)
                        ch -= key;
                    chars[i] = ch;
                }
            }
            return chars;
        }
    }

    /**
     encode/decode all
     */
    static class UnicodeAlgorithm extends Algorithm {
        public UnicodeAlgorithm(char[] chars, String operation, int key) {
            super(chars, operation, key);
        }

        @Override
        public char[] doIt() {
            if (operation.equals("enc")) {
                for (int i = 0; i < chars.length; i++) {
                    char ch = chars[i];
                    ch += key;
                    chars[i] = ch;
                }
            } else if (operation.equals("dec")) {
                for (int i = 0; i < chars.length; i++) {
                    char ch = chars[i];
                    ch -= key;
                    chars[i] = ch;
                }
            }
            return chars;
        }
    }
}


