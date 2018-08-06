package com.example.parser;

import com.example.ast.Attribute;
import com.example.ast.AttributeType;
import com.example.ast.Component;
import com.example.ast.Components;
import com.example.ast.Constant;
import com.example.ast.EditTextComponent;
import com.example.ast.HeightType;
import com.example.ast.IntegerValue;
import com.example.ast.NameComponent;
import com.example.ast.OrientationType;
import com.example.ast.Properties;
import com.example.ast.Property;
import com.example.ast.SizeAttribute;
import com.example.ast.StringValue;
import com.example.ast.TextAttribute;
import com.example.ast.TextViewComponent;
import com.example.ast.Type;
import com.example.ast.Value;
import com.example.ast.View;
import com.example.ast.WidthType;
import com.example.lexer.Lexer;
import com.example.lexer.Token;
import com.example.lexer.TokenType;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Lexer lexer;
    private Token token;
    private Token errorToken;

    private int errors;

    private ArrayList<Property> properties = new ArrayList<>();
    private ArrayList<Component> components = new ArrayList<>();

    private ArrayList<String> errorList = new ArrayList<>();

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

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public ArrayList<Component> getComponents() {
        return components;
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

        Components componentsView = parseComponents();

        eat(TokenType.CLOSE_REGION);
        eat(TokenType.EOF);

        return new View(componentsView, propertiesView);
    }

    //<Properties>  ::=   <Property> <Properties>
    //                |   ',' <Properties>
    //                |   EPSILON
    private Properties parseProperties() throws IOException {
        Properties propertiesView = new Properties();

        while (token.getType() == TokenType.WIDTH ||
                token.getType() == TokenType.HEIGHT ||
                token.getType() == TokenType.ORIENTATION) {
            Property property = parseProperty();
            propertiesView.addElement(property);
            getProperties().add(property);

            if (token.getType() == TokenType.COMMA)
                eat(TokenType.COMMA);
        }

        return propertiesView;
    }

    //<Property>    ::=   'width' '=' <Constant>
    //                |   'height' '=' <Constant>
    //                |   'orientation' '=' <Constant>
    private Property parseProperty() throws IOException {
        Type type = parseType();
        eat(TokenType.ASSIGN_VALUE);
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

    //<Components>  ::=   <Component><Components>
    //                |   EPSILON
    private Components parseComponents() throws IOException {
        Components componentsView = new Components();

        while (token.getType() == TokenType.EDIT_TEXT ||
                token.getType() == TokenType.TEXT_VIEW) {
            Component component = parseComponent();
            componentsView.addElement(component);
            getComponents().add(component);
        }

        return componentsView;
    }

    //<Component>   ::=   'editText' '(' <Attribute> ')' ';'
    //                |   'textView' '(' <Attribute> ')' ';'
    private Component parseComponent() throws IOException {
        NameComponent nameComponent = parseNameComponent();
        eat(TokenType.OPEN_PROPERTIES);

        List<Attribute> attributeList = new ArrayList<>();
        Attribute attribute = parseAttribute();

        if (attribute != null) {
            attributeList.add(attribute);

            if (token.getType() == TokenType.COMMA) {
                eat(TokenType.COMMA);

                if (token.getType() == TokenType.TEXT_SIZE || token.getType() == TokenType.TEXT_VALUE) {
                    Attribute attribute2 = parseAttribute();

                    if (attribute2 != null) {
                        attributeList.add(attribute2);
                    } else {
                        eat(TokenType.COMPONENT);
                        return null;
                    }

                }
            }

            eat(TokenType.CLOSE_PROPERTIES);
            eat(TokenType.SEMI);

            return new Component(nameComponent, attributeList);
        } else {
            eat(TokenType.COMPONENT);
            return null;
        }
    }

    private NameComponent parseNameComponent() throws IOException {
        if (token.getType() == TokenType.EDIT_TEXT) {
            eat(TokenType.EDIT_TEXT);
            return new EditTextComponent();
        } else if (token.getType() == TokenType.TEXT_VIEW) {
            eat(TokenType.TEXT_VIEW);
            return new TextViewComponent();
        }

        eat(TokenType.NAME_COMPONENT);
        return null;
    }

    //<Attribute>   ::=   'text' '=' '\\"' <String> '\\"'
    //                |   'textSize' '=' <Size> ',' 'text' '=' '\\"' <String> '\\"'
    private Attribute parseAttribute() throws IOException {
        AttributeType attributeType = null;
        Value value = null;

        if (token.getType() == TokenType.TEXT_VALUE) {
            attributeType = new TextAttribute();
            eat(TokenType.TEXT_VALUE);
            eat(TokenType.ASSIGN_VALUE);

            if (token.getType() == TokenType.STRING) {
                value = new StringValue(parseStringValue());
                return new Attribute(attributeType, value);
            } else {
                eat(TokenType.ATTRIBUTE);
                return null;
            }

        } else if (token.getType() == TokenType.TEXT_SIZE) {
            attributeType = new SizeAttribute();
            eat(TokenType.TEXT_SIZE);
            eat(TokenType.ASSIGN_VALUE);

            if (token.getType() == TokenType.DIGIT) { // TODO: Aquí es
                value = new IntegerValue(parseIntegerValue());
                return new Attribute(attributeType, value);
            } else {
                eat(TokenType.ATTRIBUTE);
                return null;
            }
        }

        eat(TokenType.ATTRIBUTE);
        return null;
    }

    //<Size>        ::=   <Digit> <Digit>
    //<Digit>       ::=   '0...9'
    private int parseIntegerValue() throws IOException {
        int value = 0;

        if (token.getType() == TokenType.DIGIT)
            value = token.getAttribute().getIntVal();

        eat(TokenType.DIGIT);

        return value;
    }

    //<String>      ::=   'a-zA-Z'
    private String parseStringValue() throws IOException {
        String value = "";

        if (token.getType() == TokenType.STRING)
            value = token.getAttribute().getIdVal();

        eat(TokenType.STRING);

        return value;
    }

    //<Constant>    ::=   'MATCH_PARENT'
    //                |   'WRAP_CONTENT'
    //                |   'HORIZONTAL'
    //                |   'VERTICAL'
    private Constant parseConstant() throws IOException {
        Constant value = null;
        if (token.getType() == TokenType.MATCH_PARENT || token.getType() == TokenType.WRAP_CONTENT ||
                token.getType() == TokenType.VERTICAL || token.getType() == TokenType.HORIZONTAL) {
            value = new Constant(token.getAttribute().getIdVal());
            eat(token.getType());
        }

        return value;
    }

}
