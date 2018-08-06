package com.example.parser;

import com.example.ast.View;
import com.example.visitor.PrintVisitor;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class TestParser {
    public static void main(String[] args) throws IOException {

        FileReader file = null;
        String fileName = "lexer01.txt";
        String path = "/home/AndroidStudioProjects/TestCompiler/compiler/src/main/java/com/example/tests/" + fileName;
        // attempt to open file
        try {
            file = new FileReader(path);
        } catch (FileNotFoundException e) {
            System.err.println(fileName + " was not found!\n");
        }

        Parser parser = new Parser(file);
        System.out.println("\nParsing " + fileName + "...\n");

        // initiate parse and clock time
        long startTime = System.currentTimeMillis();
        View program = parser.parseView();
        long endTime = System.currentTimeMillis();

        // print out statistics
        System.out.println("File has finished parsing!");
        System.out.println("Execution time: " + (endTime - startTime) + "ms");
        System.out.println(parser.getErrors() + " errors reported");
        System.out.println("---");

        // print out ASTs
        PrintVisitor printer = new PrintVisitor();
        printer.visit(program);
        System.out.println();
    }
}
