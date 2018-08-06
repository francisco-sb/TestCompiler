package com.example.ast;

import com.example.visitor.Visitor;

import java.util.List;

public class EditTextComponent implements NameComponent {
    public void accept(Visitor v) {
        v.visit(this);
    }
}
