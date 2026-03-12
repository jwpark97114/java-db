package com.db;

import java.util.Scanner;

public class DBEntryPoint {
    public static void main(String[] args) {

        InputParser userInputParser = new InputParser(myStorage);
        Scanner inputScanner = new Scanner(System.in);
        String userInput = inputScanner.nextLine();
        String result = userInputParser.runCommand(userInput);
        while(!result.equalsIgnoreCase("EXIT")){
            System.out.println(result);
            userInput = inputScanner.nextLine();
            result = userInputParser.runCommand(userInput);
        }
    }
}
