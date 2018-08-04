package com.example.ast;

import com.example.visitor.Visitor;

public class Property {
    private Type type;
    private Constant value;

    public Property(Type type, Constant value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public Constant getValue() {
        return value;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
