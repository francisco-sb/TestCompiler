package com.example.fmsierra.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ast.Attribute;
import com.example.ast.Component;
import com.example.ast.Components;
import com.example.ast.EditTextComponent;
import com.example.ast.IntegerValue;
import com.example.ast.OrientationType;
import com.example.ast.Property;
import com.example.ast.SizeAttribute;
import com.example.ast.StringValue;
import com.example.ast.TextAttribute;
import com.example.ast.TextViewComponent;
import com.example.ast.WidthType;

import java.util.List;

public class ResultDialogFragment extends DialogFragment {
    private List<Property> properties;
    private List<Component> components;

    public ResultDialogFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_result, container);

        LinearLayout parent = view.findViewById(R.id.container_result);

        LinearLayout linearLayout = new LinearLayout(getContext());

        for (int i = 0; i < components.size(); i++)
            createComponent(linearLayout, components.get(i));

        setOrientationLayout(linearLayout);
        parent.addView(linearLayout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    private void setOrientationLayout(LinearLayout linearLayout) {
        boolean orientation = false;
        for (int i = 0; i < properties.size(); i++) {
            Property property = properties.get(i);

            if (property.getType() instanceof OrientationType) {
                if (property.getValue().getValue().equals("VERTICAL"))
                    orientation = false;
                else if (property.getValue().getValue().equals("HORIZONTAL"))
                    orientation = true;
            }
        }

        if (orientation)
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        else
            linearLayout.setOrientation(LinearLayout.VERTICAL);
    }

    private void createComponent(LinearLayout parent, Component component) {
        if (component.getComponent() instanceof EditTextComponent) {
            EditText editText = new EditText(getContext());

            for (int i = 0; i < component.getAttributes().size(); i++) {
                Attribute attribute = component.getAttributes().get(i);

                if (attribute.getType() instanceof TextAttribute) {
                    StringValue value = (StringValue) attribute.getValue();
                    editText.setHint(value.getValue().substring(1, value.getValue().length() - 1));
                } else if (attribute.getType() instanceof SizeAttribute) {
                    IntegerValue value = (IntegerValue) attribute.getValue();
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, value.getValue());
                }
            }

            parent.addView(editText);

        } else if (component.getComponent() instanceof TextViewComponent) {
            TextView textView = new TextView(getContext());

            for (int i = 0; i < component.getAttributes().size(); i++) {
                Attribute attribute = component.getAttributes().get(i);

                if (attribute.getType() instanceof TextAttribute) {
                    StringValue value = (StringValue) attribute.getValue();
                    textView.setText(value.getValue().substring(1, value.getValue().length() - 1));
                } else if (attribute.getType() instanceof SizeAttribute) {
                    IntegerValue value = (IntegerValue) attribute.getValue();
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, value.getValue());
                }
            }

            parent.addView(textView);
        }
    }

}
