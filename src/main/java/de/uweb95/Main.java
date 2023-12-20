package de.uweb95;

import java.io.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        if (args.length != 2) printHelp();

        String mode = args[0];

        if (!mode.equals("encrypt") && !mode.equals("decrypt")) printHelp();

        String filename = args[1];

        File inputFile = new File(filename);

        if (!inputFile.exists()) {
            System.out.println("File " + filename + " does not exist.");
            System.exit(1);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Password: ");
        String password = scanner.nextLine();

        Encryption encryption = new Encryption();

        try {
            if (mode.equals("encrypt")) {
                encryption.encryptFile(inputFile, password);
            } else {
                encryption.decryptFile(inputFile, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printHelp() {
        System.out.println("Usage: java -jar encryption.jar <mode> <filename>");
        System.out.println("mode: encrypt or decrypt");
        System.out.println("filename: name of the file to encrypt or decrypt");
        System.exit(1);
    }
}