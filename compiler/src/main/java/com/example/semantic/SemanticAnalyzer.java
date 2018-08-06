package com.example.semantic;

import com.example.ast.Attribute;
import com.example.ast.AttributeType;
import com.example.ast.Component;
import com.example.ast.Constant;
import com.example.ast.EditTextComponent;
import com.example.ast.HeightType;
import com.example.ast.IntegerValue;
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
        parser.parseView();
        properties = parser.getProperties();
        checkProperties();
        components = parser.getComponents();
        if (components.size() > 0)
            checkComponents();
        else
            error(ErrorType.NO_COMPONENTS_FOUND);
    }

    private void checkProperties() {
        for (int i = 0; i < properties.size(); i++) {
            Property property = properties.get(i);

            if (property != null) {
                Type type = property.getType();
                Constant value = property.getValue();

                if (!isTypeValid(type))
                    error(ErrorType.PROPERTY_TYPE_NOT_EXISTS);
                if (!isValueConstant(value))
                    error(ErrorType.CONSTANT_VALUE_NOT_EXISTS);
            } else {
                error(ErrorType.BAD_PROPERTY_DECLARATION);
            }
        }
    }

    private void checkComponents() {
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);

            if (component!= null && isComponent(component)) {
                List<Attribute> attributes = component.getAttributes();

                for (int j = 0; j < attributes.size(); j++) {
                    Attribute attribute = attributes.get(j);

                    if (attribute != null) {
                        AttributeType type = attribute.getType();
                        Value value = attribute.getValue();

                        if (!isAttributeValid(type))
                            error(ErrorType.ATTRIBUTE_NOT_EXISTS);

                        if (!isValueValid(value))
                            error(ErrorType.VALUE_INVALID);

                    } else {
                        error(ErrorType.BAD_ATTRIBUTE_DECLARATION);
                    }
                }
            } else {
                error(ErrorType.BAD_COMPONENT_DECLARATION);
            }
        }
    }

    private boolean isTypeValid(Type type) {
        return type != null && (type instanceof WidthType ||
                type instanceof HeightType || type instanceof OrientationType);

    }

    private boolean isValueConstant(Constant constant) {
        return constant.getValue().equals("MATCH_PARENT") || constant.getValue().equals("WRAP_CONTENT") ||
                constant.getValue().equals("VERTICAL") || constant.getValue().equals("HORIZONTAL");
    }

    private boolean isComponent(Component component) {
        return component.getComponent() instanceof EditTextComponent || component.getComponent() instanceof TextViewComponent;
    }

    private boolean isAttributeValid(AttributeType attributeType) {
        return attributeType != null && (attributeType instanceof TextAttribute ||
                attributeType instanceof SizeAttribute);
    }

    private boolean isValueValid(Value value) {
        return value != null && (value instanceof StringValue ||
                value instanceof IntegerValue);
    }

    private void error(ErrorType errorType) {
        errors++;

        switch (errorType) {
            case NO_COMPONENTS_FOUND:
                System.err.println("Fatal Error: NO_COMPONENTS_FOUND");
                errorList.add("Fatal Error: NO_COMPONENTS_FOUND");
                break;
            case BAD_PROPERTY_DECLARATION:
                System.err.println("Declaration Error: PROPERTY_DECLARATION");
                errorList.add("Declaration Error: PROPERTY_DECLARATION");
                break;
            case BAD_COMPONENT_DECLARATION:
                System.err.println("Declaration Error: COMPONENT_DECLARATION");
                errorList.add("Declaration Error: COMPONENT_DECLARATION");
                break;
            case BAD_ATTRIBUTE_DECLARATION:
                System.err.println("Declaration Error: ATTRIBUTE_DECLARATION");
                errorList.add("Declaration Error: ATTRIBUTE_DECLARATION");
                break;
            case PROPERTY_TYPE_NOT_EXISTS:
                System.err.println("Declaration Error: PROPERTY_TYPE_NOT_EXISTS");
                errorList.add("Declaration Error: PROPERTY_TYPE_NOT_EXISTS");
                break;
            case CONSTANT_VALUE_NOT_EXISTS:
                System.err.println("Assignation Error: CONSTANT_VALUE_NOT_EXISTS");
                errorList.add("Assignation Error: CONSTANT_VALUE_NOT_EXISTS");
                break;
            case ATTRIBUTE_NOT_EXISTS:
                System.err.println("Declaration Error: ATTRIBUTE_NOT_EXISTS");
                errorList.add("Declaration Error: ATTRIBUTE_NOT_EXISTS");
                break;
            case VALUE_INVALID:
                System.err.println("Assignation Error: VALUE_INVALID");
                errorList.add("Assignation Error: VALUE_INVALID");
                break;
            default:
                break;
        }
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }
}
