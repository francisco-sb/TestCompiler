package com.example.syntax;

import com.example.ast.Block;
import com.example.ast.CProperties;
import com.example.ast.CProperty;
import com.example.ast.Component;
import com.example.ast.Components;
import com.example.ast.ViewAndroid;
import com.example.lexer.Lexer;
import com.example.lexer.Token;
import com.example.lexer.TokenType;

import java.io.FileReader;
import java.io.IOException;

public class Parser {
    private Lexer lexer;
    private Token token;
    private Token errorToken;

    private int errors;

    public Parser(FileReader file) throws IOException {
        this.lexer = new Lexer(file);
        this.token = lexer.getToken();
    }

    // verifies current token type and grabs next token or reports error
    private boolean eat(TokenType type) throws IOException {
        if (token.getType() == type) {
            token = lexer.getToken();
            return true;
        } else {
            error(type);
            return false;
        }
    }

    // reports an error to the console
    private void error(TokenType type) {
        // only report error once per erroneous token
        if (token == errorToken)
            return;

        // print error report
        System.err.print("ERROR: " + token.getType());
        System.err.print(" at line " + token.getLineNumber() + ", column " + token.getColumnNumber());
        System.err.println("; Expected " + type);

        errorToken = token; // set error token to prevent cascading
        errors++; // increment error counter
    }

    // skip tokens until match in follow set for error recovery
    private void skipTo(TokenType... follow) throws IOException {
        while (token.getType() != TokenType.EOF) {
            for (TokenType skip : follow) {
                if (token.getType() == skip)
                    return;
            }
            token = lexer.getToken();
        }
    }

    // number of reported syntax errors
    public int getErrors() {
        return errors;
    }

    //<View>    ::= linearLayout "(" <CProperties> ")" "{" <Block> "}"
    public ViewAndroid parseViewandroid() throws IOException {
        eat(TokenType.LINEAR_LAYOUT);
        eat(TokenType.OPEN_PROPERTIES);

        CProperties cProperties = parseCProperties();

        eat(TokenType.CLOSE_PROPERTIES);
        eat(TokenType.OPEN_REGION);

        Block block = parseBlock();

        eat(TokenType.CLOSE_REGION);

        return new ViewAndroid(cProperties, block);
    }

    //<CProperties>   ::= <CProperty><CProperties>
    //                  | "," <CProperties>
    //                  | EPSILON
    public CProperties parseCProperties() {
        return new CProperties();
    }

    //<CProperty>   ::= <Property> "=" <Constant>
    public CProperty parseCProperty() {
        return new CProperty();
    }

    //<Block>     ::= <RestBlock><Block>
    //              | ";" <Block>
    //              | EPSILON
    public Block parseBlock() {
        return new Block();
    }

    //<RestBlock>   ::= <Component> "(" <ComProperties> ")"
    public Components parseRestBlock() {
        return new Components();
    }

    //<Component>   ::= <editText>
    //        | <textView>
    public Component parseComponent() {
        return new Component();
    }

    //<Property>    ::= <width>
    //        | <height>
    //        | <orientation>
    // Reemplazar a un solo valor

    //<ComProperties> ::= <ComText>
    //        | <ComText> "," <ComOther>
    //<ComText>   ::= <text> "="  "\\"" <TextValue> "\\""
    //<ComOther>    ::= <ComSize> "=" <Integer>
    //<ComSize>   ::= <textSize>
    //<TextValue>   ::= [-A-Za-z_]+
    //<Integer>   ::= 0...9 <Integer>
    //        | EPSILON
    //<Constant>    ::= <MATCH_PARENT>
    //        | <WRAP_CONTENT>
    //        | <VERTICAL>
    //        | <HORIZONTAL>
}
