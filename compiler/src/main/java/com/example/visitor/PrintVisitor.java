package com.example.visitor;

import com.example.ast.Attribute;
import com.example.ast.Component;
import com.example.ast.Components;
import com.example.ast.Constant;
import com.example.ast.EditTextComponent;
import com.example.ast.HeightType;
import com.example.ast.IntegerValue;
import com.example.ast.OrientationType;
import com.example.ast.Properties;
import com.example.ast.Property;
import com.example.ast.SizeAttribute;
import com.example.ast.StringValue;
import com.example.ast.TextAttribute;
import com.example.ast.TextViewComponent;
import com.example.ast.View;
import com.example.ast.WidthType;

public class PrintVisitor implements Visitor {
    @Override
    public void visit(View view) {
        if (view.getComponents() != null)
            view.getComponents().accept(this);
        System.out.println();
    }

    @Override
    public void visit(Properties properties) {
        properties.accept(this);
        if (properties.getList() != null) {
            System.out.println("(");
            for (Property property : properties.getList()) {
                System.out.println(properties);
                System.out.println(",");
            }
            System.out.println(")");
        }
        else System.out.println("()");
        System.out.println();
    }

    @Override
    public void visit(Property property) {
        if (property.getType() != null && property.getValue() != null)
            property.accept(this);
        System.out.println();
    }

    @Override
    public void visit(WidthType widthType) {
        System.out.println("width");
    }

    @Override
    public void visit(HeightType heightType) {
        System.out.println("height");
    }

    @Override
    public void visit(OrientationType orientationType) {
        System.out.println("orientation");
    }

    @Override
    public void visit(Constant constant) {
        System.out.println(constant.getValue());
    }

    @Override
    public void visit(Components components) {
        if (components.getList() != null) {
            System.out.println("{");
            for (Component component : components.getList()) {
                System.out.println(component);
                System.out.println(";");
            }
            System.out.println("}");

        }
    }

    @Override
    public void visit(Component component) {

    }

    @Override
    public void visit(EditTextComponent editTextComponent) {
        System.out.println("editText");
    }

    @Override
    public void visit(TextViewComponent textViewComponent) {
        System.out.println("textView");
    }

    @Override
    public void visit(Attribute attribute) {

    }

    @Override
    public void visit(TextAttribute textAttribute) {
        System.out.println("text");
    }

    @Override
    public void visit(SizeAttribute sizeAttribute) {
        System.out.println("textSize");
    }

    @Override
    public void visit(IntegerValue integerValue) {
        System.out.println(integerValue);
    }

    @Override
    public void visit(StringValue stringValue) {
        System.out.println(stringValue);
    }
}
