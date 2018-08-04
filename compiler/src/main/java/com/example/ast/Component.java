package com.example.ast;

import com.example.visitor.Visitor;

import java.util.List;

public class Component {
    private NameComponent component;
    private List<Attribute> attributes;

    public Component(NameComponent component, List<Attribute> attributes) {
        this.component = component;
        this.attributes = attributes;
    }

    public NameComponent getComponent() {
        return component;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
