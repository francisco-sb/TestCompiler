package com.example.ast;

import com.example.visitor.Visitor;

public class TextAttribute implements AttributeType {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
