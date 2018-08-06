package com.example.ast;

import com.example.visitor.Visitor;

public class StringValue implements Value {
    String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
