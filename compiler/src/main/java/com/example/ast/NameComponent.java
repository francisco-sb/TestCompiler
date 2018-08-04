package com.example.ast;

import com.example.visitor.Visitor;

interface NameComponent {
    void accept(Visitor v);
}
