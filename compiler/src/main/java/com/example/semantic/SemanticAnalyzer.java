package com.example.semantic;

import com.example.ast.Attribute;
import com.example.ast.AttributeType;
import com.example.ast.Component;
import com.example.ast.Constant;
import com.example.ast.EditTextComponent;
import com.example.ast.HeightType;
import com.example.ast.IntegerValue;
import com.example.ast.NameComponent;
import com.example.ast.OrientationType;
import com.example.ast.Property;
import com.example.ast.SizeAttribute;
import com.example.ast.StringValue;
import com.example.ast.TextAttribute;
import com.example.ast.TextViewComponent;
import com.example.ast.Type;
import com.example.ast.Value;
import com.example.ast.WidthType;
import com.example.parser.Parser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {
    private Parser parser;
    private ArrayList<Property> properties;
    private ArrayList<Component> components;

    private ArrayList<String> errorList = new ArrayList<>();

    private int errors;

    public SemanticAnalyzer(FileReader file) throws IOException {
        this.parser = new Parser(file);
    }

    // Get number of errors
    public int getErrors() {
        return errors;
    }

    public ArrayList<String> getErrorList() {
        return errorList;
    }

    public Parser getParser() {
        return parser;
    }

    // Start semantic analyzer
    public void analyzeView() throws IOException {
        this.parser.parseView();
        this.properties = this.parser.getProperties();
        checkProperties();
        this.components = this.parser.getComponents();
        checkComponents();
    }

    private void checkProperties() {
        for (int i = 0; i < properties.size(); i++) {
            Property property = properties.get(i);
            Type type = property.getType();
            Constant value = property.getValue();

            if (!isTypeValid(type) || !isValueConstant(value))
                error(ErrorType.BAD_PROPERTY_DECLARATION, property);
        }
    }

    private void checkComponents() {
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            NameComponent nameComponent = component.getComponent();
            List<Attribute> attributes = component.getAttributes();

            if (isComponent(nameComponent)) {
                for (int j = 0; j < attributes.size(); j++) {
                    Attribute attribute = attributes.get(j);
                    AttributeType type = attribute.getType();
                    Value value = attribute.getValue();

                    if (!isAttributeValid(type) || !isValueValid(value)) {
                        error(ErrorType.BAD_COMPONENT_DECLARATION, component);
                    }
                }
            }
        }
    }

    private boolean isTypeValid(Type type) {
        if (type != null && (type instanceof WidthType ||
                type instanceof HeightType || type instanceof OrientationType)) {
            return true;
        } else {
            error(ErrorType.BAD_TYPE_DECLARATION, type);
            return false;
        }

    }

    private boolean isValueConstant(Constant constant) {
        if (constant != null && (constant.getValue().equals("MATCH_PARENT") ||
                constant.getValue().equals("WRAP_CONTENT") ||
                constant.getValue().equals("VERTICAL") ||
                constant.getValue().equals("HORIZONTAL"))) {
            return true;
        } else {
            assert constant != null;
            error(ErrorType.BAD_CONSTANT_VALUE_DECLARATION, constant.getValue());
            return false;
        }
    }

    private boolean isComponent(NameComponent component) {
        if (component != null && (component instanceof EditTextComponent ||
                component instanceof TextViewComponent)) {
            return true;
        } else {
            error(ErrorType.COMPONENT_NOT_FOUND, component);
            return false;
        }
    }

    private boolean isAttributeValid(AttributeType attributeType) {
        if (attributeType != null && (attributeType instanceof TextAttribute ||
                attributeType instanceof SizeAttribute)) {
            return true;
        } else {
            error(ErrorType.BAD_ATTRIBUTE_DECLARATION, attributeType);
            return false;
        }
    }

    private boolean isValueValid(Value value) {
        if (value != null && (value instanceof StringValue ||
                value instanceof IntegerValue)) {
            return true;
        } else {
            error(ErrorType.BAD_VALUE_DECLARATION, value);
            return false;
        }
    }

    private void error(ErrorType errorType, Object error) {
        errors++;

        switch (errorType) {
            case BAD_PROPERTY_DECLARATION:
                System.err.println("Declaration Error: PROPERTY_DECLARATION, variable (" + error.getClass() + ")");
                errorList.add("Declaration Error: PROPERTY_DECLARATION, variable (" + error.getClass() + ")");
                break;
            case BAD_CONSTANT_VALUE_DECLARATION:
                System.err.println("Assignation Error: CONSTANT_VALUE, variable (" + error.getClass() + ")");
                errorList.add("Declaration Error: CONSTANT_VALUE, variable (" + error.getClass() + ")");
                break;
            case NO_COMPONENTS:
                System.err.println("Critical Error: NO_COMPONENTS, variable (" + error.getClass() + ")");
                errorList.add("Declaration Error: NO_COMPONENTS, variable (" + error.getClass() + ")");
                break;
            case BAD_ATTRIBUTE_DECLARATION:
                System.err.println("Declaration Error: ATTRIBUTE_DECLARATION, variable (" + error.getClass() + ")");
                errorList.add("Declaration Error: ATTRIBUTE_DECLARATION, variable (" + error.getClass() + ")");
                break;
            case BAD_COMPONENT_DECLARATION:
                System.err.println("Declaration Error: COMPONENT_DECLARATION, variable (" + error.getClass() + ")");
                errorList.add("Declaration Error: COMPONENT_DECLARATION, variable (" + error.getClass() + ")");
                break;
            default:
                break;
        }
    }
}
