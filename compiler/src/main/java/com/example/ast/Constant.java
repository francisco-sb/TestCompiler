package com.example.ast;

import com.example.visitor.Visitor;

public class Constant {
    private String value;

    public Constant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
