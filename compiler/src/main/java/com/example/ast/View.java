package com.example.ast;

import com.example.visitor.Visitor;

public class View {
    private Properties properties;
    private Components components;

    public View(Components components, Properties properties) {
        this.components = components;
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }

    public Components getComponents() {
        return components;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }
}
