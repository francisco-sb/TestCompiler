package com.example.ast;

import com.example.visitor.Visitor;

public class IntegerValue implements Value {
    private int value;

    public IntegerValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
