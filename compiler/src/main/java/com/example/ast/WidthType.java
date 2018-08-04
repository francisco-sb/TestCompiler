package com.example.ast;

import com.example.visitor.Visitor;

public class WidthType implements Type {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
