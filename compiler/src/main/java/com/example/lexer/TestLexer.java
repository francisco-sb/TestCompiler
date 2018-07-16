package com.example.lexer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestLexer {
    public static void main(String[] args) throws IOException {

        FileReader file = null;
        String fileName = "lexer01.txt";
        // attempt to open file
        try {
            file = new FileReader("C:\\Users\\fmsierra\\Desktop\\AndroidProjects\\Test\\lexer\\src\\main\\java\\tests\\" + fileName);
        } catch (FileNotFoundException e) {
            System.err.println(fileName + " was not found!\n");
        }

        // create lexer
        Lexer lexer = new Lexer(file);

        // start tokenizing file
        System.out.println("\nTokenizing " + fileName + "...\n");
        long startTime = System.currentTimeMillis();
        int numTokens = 0;
        Token token;
        do {
            token = lexer.getToken();
            numTokens++;

            if (token.getType() == TokenType.UNKNOWN) {
                // print token type and location
                System.err.print(token.getType());
                System.err.print(" (" + token.getLineNumber() + "," + token.getColumnNumber() + ")");
                System.out.println();
                continue;
            }

            System.out.print(token.getType());
            System.out.print(" (" + token.getLineNumber() + "," + token.getColumnNumber() + ")");

            // print out semantic values for ID and INT_CONST tokens
            if (token.getType() == TokenType.ID)
                System.out.println(": " + token.getAttribute().getIdVal());
            else if (token.getType() == TokenType.DIGIT)
                System.out.println(": " + token.getAttribute().getIntVal());
            else if (token.getType() == TokenType.MATCH_PARENT)
                System.out.println(": " + token.getAttribute().getIdVal());
            else if (token.getType() == TokenType.WRAP_CONTENT)
                System.out.println(": " + token.getAttribute().getIdVal());
            else if (token.getType() == TokenType.VERTICAL)
                System.out.println(": " + token.getAttribute().getIdVal());
            else if (token.getType() == TokenType.HORIZONTAL)
                System.out.println(": " + token.getAttribute().getIdVal());
            else
                System.out.println();

        } while (token.getType() != TokenType.EOF);

        long endTime = System.currentTimeMillis();

        // print out statistics
        System.out.println("\n\n---");
        System.out.println("Number of tokens: " + numTokens);
        System.out.println("Execution time: " + (endTime - startTime) + "ms");
        System.out.println();
    }
}
