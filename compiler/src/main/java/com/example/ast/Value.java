package com.example.ast;

import com.example.visitor.Visitor;

public interface Value {
    void accept(Visitor v);
}
