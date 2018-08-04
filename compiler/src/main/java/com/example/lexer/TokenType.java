package com.example.lexer;

public enum TokenType {
    // constants
    EOF, // input stream has been consumed
    UNKNOWN, // character/token could not be precessed
    DIGIT, // [0-9]+
    STRING, // [a-zA-Z][a-zA-Z0-9\t]*
    MATCH_PARENT, // MATCH_PARENT
    WRAP_CONTENT, // WRAP_CONTENT
    VERTICAL, // VERTICAL
    HORIZONTAL, // HORIZONTAL

    // reserved words
    LINEAR_LAYOUT, // linearLayout
    EDIT_TEXT, // editText
    TEXT_VIEW, // textView

    // properties
    ORIENTATION, // orientation
    WIDTH, // width
    HEIGHT, // height
    TEXT_VALUE, // text
    TEXT_SIZE, // textSize

    // punctuation
    OPEN_REGION, // {
    CLOSE_REGION, // }
    OPEN_PROPERTIES, // (
    CLOSE_PROPERTIES, // )
    ASSIGN_VALUE, // =
    COMMA, // ,
    SEMI, // ;

    // region id's
    ID, // [a-zA-Z]

    // for error reporting
    PROPERTY,
    COMPONENT,
    TYPE
}
