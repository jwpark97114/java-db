package com.db;

import java.util.Scanner;

public class DBEntryPoint {
    public static void main(String[] args) {
        DBEngine newEngine = new DBEngine();
        InputParser userInputParser = new InputParser(newEngine);
        Scanner inputScanner = new Scanner(System.in);
        String userInput = inputScanner.nextLine();

        while(!userInput.equalsIgnoreCase("EXIT")){
            userInputParser.runCommand(userInput);
            userInput = inputScanner.nextLine();

        }
    }
}
