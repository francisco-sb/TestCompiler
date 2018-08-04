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

public interface Visitor {

    void visit(View view);

    void visit(Properties properties);
    void visit(Property property);
    void visit(WidthType widthType);
    void visit(HeightType heightType);
    void visit(OrientationType orientationType);
    void visit(Constant constant);

    void visit(Components components);
    void visit(Component component);
    void visit(EditTextComponent editTextComponent);
    void visit(TextViewComponent textViewComponent);
    void visit(Attribute attribute);
    void visit(TextAttribute textAttribute);
    void visit(SizeAttribute sizeAttribute);
    void visit(IntegerValue integerValue);
    void visit(StringValue stringValue);
}
