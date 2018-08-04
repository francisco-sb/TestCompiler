package com.example.ast;

import com.example.visitor.Visitor;

import java.util.List;

public class Components {
    private List<Component> list;

    public Components(List<Component> list) {
        this.list = list;
    }

    public List<Component> getList() {
        return list;
    }

    public void addElement(Component component) {
        getList().add(component);
    }

    public Component elementAt(int index) {
        return getList().get(index);
    }

    public int size() {
        return getList().size();
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
