package com.example.ast;

import com.example.visitor.Visitor;

import java.util.ArrayList;
import java.util.List;

public class CProperties {
    private List<CProperty> properties;

    public CProperties() {
        properties = new ArrayList<CProperty>();
    }

    public void addElement(CProperty property) {
        getList().add(property);
    }

    public CProperty elementAt(int index) {
        return getList().get(index);
    }

    public int size() {
        return getList().size();
    }

    public List<CProperty> getList() {
        return properties;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
