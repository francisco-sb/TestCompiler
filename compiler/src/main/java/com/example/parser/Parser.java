package com.example.parser;

import com.example.ast.Attribute;
import com.example.ast.Components;
import com.example.ast.Constant;
import com.example.ast.HeightType;
import com.example.ast.OrientationType;
import com.example.ast.Properties;
import com.example.ast.Property;
import com.example.ast.Type;
import com.example.ast.View;
import com.example.ast.WidthType;
import com.example.lexer.Lexer;
import com.example.lexer.Token;
import com.example.lexer.TokenType;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    private Lexer lexer;
    private Token token;
    private Token errorToken;

    private int errors;

    private ArrayList<Properties> properties;
    private ArrayList<Components> components;
    private ArrayList<Attribute> attributes;

    private ArrayList<String> errorList = new ArrayList<>();

    public Parser(FileReader file) throws IOException {
        this.lexer = new Lexer(file);
        this.token = lexer.getToken();
        properties = new ArrayList<>();
        components = new ArrayList<>();
        attributes = new ArrayList<>();
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

        errorList.add("ERROR: " + token.getType() +
                " at line " + token.getLineNumber() + ", column " + token.getColumnNumber() + "; Expected " + type);

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

    public ArrayList<Properties> getProperties() {
        return properties;
    }

    public ArrayList<Components> getComponents() {
        return components;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public ArrayList<String> getErrorList() {
        return errorList;
    }

    //<View>        ::=   'linearLayout' '(' <Properties> ')' '{' <Components> '}'
    public View parseView() throws IOException {
        eat(TokenType.LINEAR_LAYOUT);
        eat(TokenType.OPEN_PROPERTIES);

        Properties propertiesView = parseProperties();

        eat(TokenType.CLOSE_PROPERTIES);
        eat(TokenType.OPEN_REGION);

        Components componentsView = null;//parseComponents();

        eat(TokenType.CLOSE_REGION);
        eat(TokenType.EOF);

        return new View(componentsView, propertiesView);
    }

    /*public View parseView() {
        eat(TokenType.LINEAR_LAYOUT);
        eat(TokenType.OPEN_PROPERTIES);

        List<Property> properties = parseProperties();

        eat(TokenType.CLOSE_PROPERTIES);
        eat(TokenType.OPEN_REGION);

    }*/

    //<Properties>  ::=   <Property> <Properties>
    //                |   ',' <Properties>
    //                |   EPSILON
    private Properties parseProperties() throws IOException {
        Properties propertiesView = new Properties();

        while (token.getType() == TokenType.WIDTH || token.getType() == TokenType.HEIGHT || token.getType() == TokenType.ORIENTATION)
            propertiesView.addElement(parseProperty());

        return propertiesView;
    }

    /*public List<Property> parseProperties() {
        List<Property> properties = new ArrayList()<>;

        Property property = parseProperty();

        if (property != null) {
            properties.add(property)
        }

        return properties;
    }*/

    //<Property>    ::=   'width' '=' <Constant>
    //                |   'height' '=' <Constant>
    //                |   'orientation' '=' <Constant>
    private Property parseProperty() throws IOException {
        Type type = parseType();
        Constant value = parseConstant();

        return new Property(type, value);
    }

    private Type parseType() throws IOException {
        if (token.getType() == TokenType.WIDTH) {
            eat(TokenType.WIDTH);
            return new WidthType();
        } else if (token.getType() == TokenType.HEIGHT) {
            eat(TokenType.HEIGHT);
            return new HeightType();
        } else if (token.getType() == TokenType.ORIENTATION) {
            eat(TokenType.ORIENTATION);
            return new OrientationType();
        }

        // invalid type declaration
        eat(TokenType.TYPE);
        return null;
    }

    /*public Property parseProperty() {
        Property property = new Property();

        if (token.getType() == TokenType.WIDTH || token.getType() == TokenType.HEIGHT || token.getType == TokenType.ORIENTATION) {
            property.setType(token.getType);
            eat(token.getType());

            eat(TokenType.ASSIGN_VALUE);

            property.setConstant(token.getType)
            eat(TokenType.getType())
        }

        return property;
    }*/

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
    private Constant parseConstant() throws IOException {
        Constant value = null;
        while (token.getType() == TokenType.MATCH_PARENT || token.getType() == TokenType.WRAP_CONTENT ||
                token.getType() == TokenType.VERTICAL || token.getType() == TokenType.HORIZONTAL)
            value = new Constant(token.getAttribute().getIdVal());

        return value;
    }

}
