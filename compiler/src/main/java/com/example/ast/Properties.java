package com.example.ast;

import com.example.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class Properties {
    private List<Property> list;

    public Properties() {
        this.list = new ArrayList<>();
    }

    public List<Property> getList() {
        return list;
    }

    public void addElement(Property property) {
        getList().add(property);
    }

    public Property elementAt(int index) {
        return getList().get(index);
    }

    public int size() {
        return getList().size();
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

}
