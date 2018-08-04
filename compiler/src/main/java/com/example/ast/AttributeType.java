package com.example.ast;

import com.example.visitor.Visitor;

public interface AttributeType {
    void accept(Visitor v);
}
