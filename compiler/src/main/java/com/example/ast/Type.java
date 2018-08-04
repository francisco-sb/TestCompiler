package com.example.ast;

import com.example.visitor.Visitor;

public interface Type {
    void accept(Visitor v);
}
