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

    //<View>        ::=   'linearLayout' '(' <Properties> ')' '{' <Components> '}'
    public View parseView() {
        eat(TokenType.LINEAR_LAYOUT);
        eat(TokenType.OPEN_PROPERTIES);

        List<Property> properties = parseProperties();

        eat(TokenType.CLOSE_PROPERTIES);
        eat(TokenType.OPEN_REGION);

    }
    //<Properties>  ::=   <Property> <Properties>
    //                |   ',' <Properties>
    //                |   EPSILON
    public List<Property> parseProperties() {
        List<Property> properties = new ArrayList()<>;

        Property property = parseProperty();

        if (property != null) {
            properties.add(property)
        }

        return properties;
    }
    //<Property>    ::=   'width' '=' <Constant>
    //                |   'height' '=' <Constant>
    //                |   'orientation' '=' <Constant>
    public Property parseProperty() {
        Property property = new Property();

        if (token.getType() == TokenType.WIDTH || token.getType() == TokenType.HEIGHT || token.getType == TokenType.ORIENTATION) {
            property.setType(token.getType);
            eat(token.getType());

            eat(TokenType.ASSIGN_VALUE);

            property.setConstant(token.getType)
            eat(TokenType.getType())
        }

        return property;
    }

    //<Components>  ::=   <Component><Components>
    //                |   EPSILON
    //<Component>   ::=   'editText' '(' <Attribute> ')' ';'
    //                |   'textView' '(' <Attribute> ')' ';'
    //<Attribute>   ::=   'text' '=' '\\"' <String> '\\"'
    //                |   'textSize' '=' <Size> ',' 'text' '=' '\\"' <String> '\\"'
    //<Size>        ::=   <Digit> <Digit>
    //<Digit>       ::=   '0...9'
    //<String>      ::=   'a-zA-Z'
    //<Constant>    ::=   'MATCH_PARENT'
    //                |   'WRAP_CONTENT'
    //                |   'HORIZONTAL'
    //                |   'VERTICAL'

}
