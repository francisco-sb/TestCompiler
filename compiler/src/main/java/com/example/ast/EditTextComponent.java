package com.example.ast;

import com.example.visitor.Visitor;

import java.util.List;

public class EditTextComponent implements NameComponent {
    private String nameComponent;
    private List<Attribute> attributeList;

    public EditTextComponent(String nameComponent, List<Attribute> attributeList) {
        this.nameComponent = nameComponent;
        this.attributeList = attributeList;
    }

    public String getNameComponent() {
        return nameComponent;
    }

    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }
}
