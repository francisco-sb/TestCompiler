package com.example.models;

import com.example.lexer.TokenType;

public class Property {
    private TokenType type;
    private TokenType constant;
    private String value;

    public Property() {}

    public Property(TokenType type, TokenType constant) {
        this.type = type;
        this.constant = constant;
    }

    public Property(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public void setType(TokenType type) {
        this.type = type;
    }

    public TokenType getConstant() {
        return constant;
    }

    public void setConstant(TokenType constant) {
        this.constant = constant;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
