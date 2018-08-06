package com.example.fmsierra.test;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ast.Component;
import com.example.ast.Property;
import com.example.semantic.SemanticAnalyzer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String GRAMMAR = "" +
            "<View>        ::=   'linearLayout' '(' <Properties> ')' '{' <Components> '}'\n" +
            "<Properties>  ::=   <Property> <Properties>\n" +
            "                |   ',' <Properties>\n" +
            "                |   EPSILON\n" +
            "<Property>    ::=   'width' '=' <Constant>\n" +
            "                |   'height' '=' <Constant>\n" +
            "                |   'orientation' '=' <Constant>\n" +
            "<Components>  ::=   <Component><Components>\n" +
            "                |   EPSILON\n" +
            "<Component>   ::=   'editText' '(' <Attribute> ')' ';'\n" +
            "                |   'textView' '(' <Attribute> ')' ';'\n" +
            "<Attribute>   ::=   'text' '=' '\\\\\"' <String> '\\\\\"'\n" +
            "                |   'textSize' '=' <Size> ',' 'text' '=' '\\\\\"' <String> '\\\\\"'\n" +
            "<Size>        ::=   <Digit> <Digit>\n" +
            "<Digit>       ::=   '0...9'\n" +
            "<String>      ::=   'a-zA-Z'\n" +
            "<Constant>    ::=   'MATCH_PARENT'\n" +
            "                |   'WRAP_CONTENT'\n" +
            "                |   'HORIZONTAL'\n" +
            "                |   'VERTICAL'";

    private static final String FIRST_FOLLOW = "" +
            "F  I  R  S  T \n" +
            "{   <View>      : [ 'linearLayout' ],\n" +
            "    <Properties>: [ ',', null, 'width', 'height', 'orientation' ],\n" +
            "    <Property>  : [ 'width', 'height', 'orientation' ],\n" +
            "    <Components>: [ 'editText', 'textView' ],\n" +
            "    <Component> : [ 'editText', 'textView' ],\n" +
            "    <Attribute> : [ 'text', 'textSize' ],\n" +
            "    <Size>      : [ '0' ],\n" +
            "    <Digit>     : [ '0' ],\n" +
            "    <String>    : [ 'abc' ],\n" +
            "    <Constant>  : [ 'MATCH_PARENT', 'WRAP_CONTENT', 'VERTICAL', 'HORIZONTAL' ] }\n\n" +
            "F  O  L  L  O  W \n" +
            "{   <View>      : [ '\\u0000' ],\n" +
            "    <Properties>: [ ')' ],\n" +
            "    <Property>  : [ ',', 'width', 'height', 'orientation', ')' ],\n" +
            "    <Components>: [ '}' ],\n" +
            "    <Component> : [ 'editText', 'textView' ],\n" +
            "    <Attribute> : [ ')' ],\n" +
            "    <Size>      : [ ',' ],\n" +
            "    <Digit>     : [ '0', ',' ],\n" +
            "    <String>    : [ '\\\\\"' ],\n" +
            "    <Constant>  : [ ',', 'width', 'height', 'orientation', ')' ] }";

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText = findViewById(R.id.et_to_compile);
        final Context context = this;

        Button btnGrammar = findViewById(R.id.btn_grammar);
        Button btnFirstFollow = findViewById(R.id.btn_first_follow);
        Button btnPredictiveTable = findViewById(R.id.btn_predictive_table);
        Button btnClear = findViewById(R.id.btn_clear);

        btnGrammar.setOnClickListener(this);
        btnFirstFollow.setOnClickListener(this);
        btnPredictiveTable.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fileName = "compileFile.txt";

                writeToFile(editText.getText().toString(), context);
                FileReader fileReader;

                try {
                    fileReader = new FileReader(new File(getFilesDir(), fileName));

                    // create semantic analyzer
                    SemanticAnalyzer semantic = new SemanticAnalyzer(fileReader);
                    String result = "Analizando " + fileName + "...\n\n\n";

                    // initiate parse and clock time
                    long startTime = System.currentTimeMillis();
                    semantic.analyzeView();
                    long endTime = System.currentTimeMillis();

                    // print out statistics
                    result += "" +
                            "¡Entrada analizada!\n" +
                            "Tiempo de ejecucion: " + (endTime - startTime) + "ms\n" +
                            "" + semantic.getErrors() + " errores reportados\n" +
                            "\n------------------------------------------------------\n";

                    for (int i = 0; i < semantic.getParser().getErrorList().size(); i++) {
                        result += semantic.getParser().getErrorList().get(i) + "\n";
                    }

                    result += "\n------------------------------------------------------\n";

                    for (int i = 0; i < semantic.getErrorList().size(); i++) {
                        result += semantic.getErrorList().get(i) + "\n";
                    }

                    editText.setText(result);

                    if (semantic.getErrors() == 0)
                        showResult(semantic.getProperties(), semantic.getComponents());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("compileFile.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
            Toast.makeText(context, "Exception with file creation: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_grammar:
                editText.setText(GRAMMAR);
                break;
            case R.id.btn_first_follow:
                editText.setText(FIRST_FOLLOW);
                break;
            case R.id.btn_predictive_table:
                break;
            case R.id.btn_clear:
                editText.setText("");
                break;
        }
    }

    private void showResult(List<Property> properties, List<Component> components) {
        FragmentManager fm = getSupportFragmentManager();
        ResultDialogFragment dialog = new ResultDialogFragment();
        dialog.setProperties(properties);
        dialog.setComponents(components);
        dialog.show(fm, "ResultDialogFragment");
    }
}
