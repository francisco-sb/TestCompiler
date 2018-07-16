package com.example.visitor;

import com.example.ast.*;

public interface Visitor {
    public void visit(ViewAndroid viewAndroid);
    public void visit(CProperties cProperties);
    public void visit(CProperty cProperty);
    public void visit(Block block);
    public void visit(Components components);
    public void visit(Component component);
}
