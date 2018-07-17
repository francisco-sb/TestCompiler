package com.example.models;

import com.example.lexer.TokenType;

public class Component {
    private TokenType type;
    private Property property;

    public Component(TokenType type, Property property) {
        this.type = type;
        this.property = property;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }
}
