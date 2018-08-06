package com.example.ast;

import com.example.visitor.Visitor;

public interface NameComponent {
    void accept(Visitor v);
}
