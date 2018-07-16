package com.example.lexer;

public class TokenAttribute {
    private int intVal; // int value of token
    private char charVal; // char value of token
    private String idVal; // id of token

    public TokenAttribute() {
    }

    // construct TokenAttribute with an int value
    public TokenAttribute(int intVal) {
        this.intVal = intVal;
    }

    // construct TokenAttribute with a char value
    public TokenAttribute(char charVal) {
        this.charVal = charVal;
    }

    // construct TokenAttribute with an id
    public TokenAttribute(String idVal) {
        this.idVal = idVal;
    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    public char getCharVal() {
        return charVal;
    }

    public void setCharVal(char charVal) {
        this.charVal = charVal;
    }

    public String getIdVal() {
        return idVal;
    }

    public void setIdVal(String idVal) {
        this.idVal = idVal;
    }
}
