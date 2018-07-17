package com.example.syntax;

import com.example.lexer.Token;
import com.example.lexer.TokenType;
import com.example.models.Component;
import com.example.models.Property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private Token token;
    private Token tokenError;

    private int tokenIndex = 0;
    private int errors;

    private ArrayList<Property> properties;
    private ArrayList<Component> components;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.token = tokens.get(tokenIndex);
    }

    // verifies current token type and grabs next token or reports error
    private boolean eat(TokenType type) {
        if (token.getType() == type) {
            token = tokens.get(tokenIndex++);
            return true;
        } else {
            error(type);
            return false;
        }
    }

    // reports an error to the console
    private void error(TokenType type) {
        // only report error once per erroneous token
        if (token == tokenError)
            return;

        // print error report
        System.err.print("ERROR: " + token.getType());
        System.err.print(" at line " + token.getLineNumber() + ", column " + token.getColumnNumber());
        System.err.println("; Expected " + type);

        tokenError = token; // set error token to prevent cascading
        errors++; // increment error counter
    }

    // skip tokens until match in follow set for error recovery
    private void skipTo(TokenType... follow) throws IOException {
        while (token.getType() != TokenType.EOF) {
            for (TokenType skip : follow) {
                if (token.getType() == skip)
                    return;
            }
            token = tokens.get(tokenIndex++);
        }
    }

    // number of reported syntax errors
    public int getErrors() {
        return errors;
    }


}
