package com.enrique7mc.braintrainer;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    RelativeLayout layout;
    Typeface typeface;
    android.support.v7.widget.GridLayout grid;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextView operationTextView;
    TextView resultTextView;
    Operation currentOperation;

    private static final Random random = new Random();
    private final Operator[] operators = Operator.values();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout) findViewById(R.id.container);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto.ttf");
        overrideFonts(layout);
        grid = (android.support.v7.widget.GridLayout) findViewById(R.id.grid);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);
        textView1.setOnClickListener(clickListener);
        textView2.setOnClickListener(clickListener);
        textView3.setOnClickListener(clickListener);
        textView4.setOnClickListener(clickListener);
        operationTextView = (TextView) findViewById(R.id.operationTextView);
        resultTextView = (TextView) findViewById(R.id.resultTextView);

        currentOperation = generateOperation();
        setAnswers(currentOperation);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int response = Integer.parseInt(((TextView)v).getText().toString());
            Log.i(TAG, response + "");
            if (response == currentOperation.getResult()) {
                resultTextView.setText("Correct!");
            } else {
                resultTextView.setText("Wrong!");
            }

            currentOperation = generateOperation();
            setAnswers(currentOperation);
        }
    };

    private Operation generateOperation() {
        int left = random.nextInt(20);
        int right = random.nextInt(20);
        int operatorIndex = random.nextInt(3);
        Operation op = new Operation(left, right, operators[operatorIndex]);
        return op;
    }

    private Integer[] generateAnswers(Operation op) {
        int result = op.getResult();
        List<Integer> answers = new ArrayList<>();
        answers.add(result);

        while(answers.size() < 4) {
            boolean isPositive = random.nextBoolean();
            int offset = random.nextInt(Math.abs(result) + 1);
            int answer = isPositive ? result + offset : result - offset;
            if (!answers.contains(answer)) {
                answers.add(answer);
            }
        }

        Collections.shuffle(answers);
        return answers.toArray(new Integer[answers.size()]);
    }

    private void setAnswers(Operation op) {
        operationTextView.setText(op.toString());
        Integer[] answers = generateAnswers(op);

        textView1.setText(String.valueOf(answers[0]));
        textView2.setText(String.valueOf(answers[1]));
        textView3.setText(String.valueOf(answers[2]));
        textView4.setText(String.valueOf(answers[3]));
    }

    private void overrideFonts(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(typeface);
            }
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }
}
