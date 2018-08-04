package com.example.ast;

import com.example.visitor.Visitor;

public class Attribute {
    private AttributeType type;
    private Value value;

    public Attribute(AttributeType type, Value value) {
        this.type = type;
        this.value = value;
    }

    public AttributeType getType() {
        return type;
    }

    public Value getValue() {
        return value;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
