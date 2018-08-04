package com.example.ast;

import com.example.visitor.Visitor;

public class StringValue implements Value {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
