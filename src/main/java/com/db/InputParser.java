package com.db;

public class InputParser {

    DBEngine dbEngine;

    public InputParser(DBEngine sqlEngine){
        this.dbEngine = sqlEngine;
    }

    public void runCommand(String userInput){
        String[] keywordAndELse = userInput.split(" ",2);
        String queryLine = keywordAndELse[1].strip();
        switch (keywordAndELse[0]){
            case "CREATE" -> {
                String[] isTableInHere = queryLine.split(" ",2);
                if(!isTableInHere[0].equals("TABLE")) return;
                dbEngine.createTable(isTableInHere[1]);
            }
            case "INSERT" -> dbEngine.insertIntoTable(queryLine);
            case "SELECT" -> dbEngine.selectFromTable(queryLine);
            default -> System.out.println("Unknown Command Try Again");
        }
    }
}
